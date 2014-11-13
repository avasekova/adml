####hrozne hnusne upraveny, ale snad funkcny Holt pre intervaly. TODO skraslit! a skontrolovat, ci ide vobec ako ma.
#volat ako "A.holt(daaata, h=10, alpha=alpha, beta=beta, gamma=FALSE)", kde daaata je dvojstlpcova matica, kde prvy stlpec je LB a druhy UB

### Modelled on the HoltWinters() function but with more conventional
### initialization.
### Written by Zhenyu Zhou. 21 October 2012
### Adapted for intervals by AV.

A.HoltWintersZZ  <- function (x,
                            # smoothing parameters
                            alpha    = NULL, # level
                            beta     = NULL, # trend
                            gamma    = NULL, # seasonal component
                            seasonal = c("additive", "multiplicative"),
                            exponential = FALSE, # exponential
                            phi = NULL # damp
)
{
  x <- as.ts(x)
  seasonal <- match.arg(seasonal)
  m <- frequency(x)
  lenx <- length(x)/2   #####################################A, bo mam dva stlpce!
  
  if(is.null(phi) || !is.numeric(phi))
    phi <- 1
  if(!is.null(alpha) && !is.numeric(alpha))
    stop ("cannot fit models without level ('alpha' must not be 0 or FALSE).")
  if(!all(is.null(c(alpha, beta, gamma))) &&
       any(c(alpha, beta, gamma) < 0 || c(alpha, beta, gamma) > 1))
    stop ("'alpha', 'beta' and 'gamma' must be within the unit interval.")
  if((is.null(gamma) || gamma > 0)) {
    if (seasonal == "multiplicative" && any(x <= 0))
      stop ("data must be positive for multiplicative Holt-Winters.")
  }
  
  if(m<=1)
    gamma <- FALSE
  
  ## initialise l0, b0, s0
  if(!is.null(gamma) && is.logical(gamma) && !gamma) {
    seasonal <- "none"
    l.start <- x[1L,]
    s.start <- cbind(c(0,0))
    if(is.null(beta) || !is.logical(beta) || beta){
      if(!exponential)
        b.start <- x[2L,] - x[1L,]
      else
        b.start <- x[2L,]%/%x[1L,]
    }
  } else {
    ## seasonal Holt-Winters
    l.start <- cbind(c(mean(x[1:m,]), mean(x[1:m,]))) #dvakrat to iste
    b.start <- (mean(x[m+(1:m),]) - l.start)/m
    if(seasonal=="additive")
      s.start <- x[1:m,]-l.start
    else
      s.start <- x[1:m,]%/%l.start
  }
  
  #initialise smoothing parameters
  #lower=c(rep(0.0001,3), 0.8)
  #upper=c(rep(0.9999,3),0.98)
  lower <- c(0,0,0,0)
  upper <- c(1,1,1,1)
  
  if(!is.null(beta) && is.logical(beta) && !beta)
    trendtype <- "N"
  else if(exponential)
    trendtype <- "M"
  else
    trendtype <- "A"
  
  if(seasonal=="none")
    seasontype <- "N"
  else if(seasonal=="multiplicative")
    seasontype <- "M"
  else
    seasontype <- "A"
  
  ## initialise smoothing parameter
  optim.start <- A.initparam(alpha = alpha, beta = beta, gamma=gamma, phi=1,
                           trendtype=trendtype, seasontype=seasontype, damped=FALSE, lower=lower, upper=upper, m=m)
  
  # if(!is.na(optim.start["alpha"]))
  # 	alpha2 <- optim.start["alpha"]
  # else
  # 	alpha2 <- alpha
  # if(!is.na(optim.start["beta"]))
  # 	beta2 <- optim.start["beta"]
  # else
  # 	beta2 <- beta
  # if(!is.na(optim.start["gamma"]))
  # 	gamma2 <- optim.start["gamma"]
  # else
  # 	gamma2 <- gamma
  
  #	if(!check.param(alpha = alpha2,beta = beta2, gamma = gamma2,phi=1,lower,upper,bounds="haha",m=m))
  #	{
  #		print(paste("alpha=", alpha2, "beta=",beta2, "gamma=",gamma2))
  #		stop("Parameters out of range")
  #	}
  
  ###################################################################################
  #optimisation: alpha, beta, gamma, if any of them is null, then optimise them
  error <- function (p, select)
  {
    if(select[1]>0)
      alpha <- p[1L]
    if(select[2]>0)
      beta <- p[1L+select[1]]
    if(select[3]>0)
      gamma <- p[1L+select[1]+select[2]]
    
    zzhw(x,lenx=lenx, alpha = alpha, beta=beta, gamma=gamma, seasonal=seasonal, m=m,
         dotrend=(!is.logical(beta) || beta), doseasonal=(!is.logical(gamma) || gamma),
         exponential=exponential, phi=phi, l.start=l.start, b.start=b.start, s.start=s.start)$SSE
  }
  select <- as.numeric(c(is.null(alpha),is.null(beta),is.null(gamma)))
  
  if(sum(select)>0) # There are parameters to optimize
  {
    sol <- optim(optim.start, error, method = "L-BFGS-B", lower = lower[select], upper = upper[select], select=select)
    if(sol$convergence || any(sol$par < 0 | sol$par > 1)) {
      if (sol$convergence > 50) {
        warning(gettextf("optimization difficulties: %s", sol$message), domain = NA)
      } else stop("optimization failure")
    }
    if(select[1]>0)
      alpha <- sol$p[1L]
    if(select[2]>0)
      beta <- sol$p[1L+select[1]]
    if(select[3]>0)
      gamma <- sol$p[1L+select[1]+select[2]]
  }
  
  final.fit <- A.zzhw(x, lenx=lenx, alpha = alpha, beta=beta, gamma=gamma, seasonal=seasonal, m=m,
                    dotrend=(!is.logical(beta) || beta), doseasonal=(!is.logical(gamma) || gamma),
                    exponential=exponential, phi=phi, l.start=l.start, b.start=b.start, s.start=s.start)
  
  tspx <- tsp(x)
  fitted <- ts(final.fit$fitted,frequency=m,start=tspx[1])
  states <- matrix(final.fit$level,ncol=1)
  colnames(states) <- "l"
  if(trendtype!="N")
    states <- cbind(states,b=final.fit$trend)
  if(seasontype!="N")
  {
    nr <- nrow(states)
    nc <- ncol(states)
    for(i in 1:m)
      states <- cbind(states,final.fit$season[(m-i)+(1:nr)])
    colnames(states)[nc+(1:m)] <- paste("s",1:m,sep="")
  }
  states <- ts(states,frequency=m,start=tspx[1]-1/m)
  
  # Package output as HoltWinters class
  # structure(list(fitted    = fitted,
  # 				x        = x,
  # 				alpha    = alpha,
  # 				beta     = beta,
  # 				gamma    = gamma,
  # 				coefficients = c(a = final.fit$level[lenx],
  # 						b = if (!is.logical(beta) || beta) final.fit$trend[lenx],
  # 						s = if (!is.logical(gamma) || gamma) final.fit$season[lenx - m + 1L:m]),
  # 				seasonal  = seasonal,
  # 				exponential = exponential,
  # 				SSE       = final.fit$SSE,
  # 				call      = match.call(),
  # 				level = final.fit$level,
  # 				trend = final.fit$trend,
  # 				season = final.fit$season,
  # 				phi = phi
  # 		),
  # 		class = "HoltWinters"
  # )
  # Package output as ets class
  damped <- (phi<1.0)
  if(seasonal=="additive") # This should not happen
    components <- c("A",trendtype,seasontype,damped)
  else if(seasonal=="multiplicative")
    components <- c("M",trendtype,seasontype, damped)
  else if(seasonal=="none" & exponential)
    components <- c("M",trendtype,seasontype,damped)
  else# if(seasonal=="none" & !exponential)
    components <- c("A",trendtype,seasontype, damped)
  
  initstate <- states[1,]
  param <- alpha
  names(param) <- "alpha"
  if(trendtype!="N")
  {
    param <- c(param,beta=beta)
    names(param)[length(param)] <- "beta"
  }
  if(seasontype!="N")
  {
    param <- c(param,gamma=gamma)
    names(param)[length(param)] <- "gamma"
  }
  if(damped)
  {
    param <- c(param,phi=phi)
    names(param)[length(param)] <- "phi"
  }
  
  if(components[1]=="A")
    sigma2 <- mean(final.fit$residuals^2)
  else
    sigma2 <- mean((final.fit$residuals/fitted)^2)
  structure(list(fitted    = fitted,
                 residuals=final.fit$residuals,
                 components=components,
                 x        = x,
                 par=c(param,initstate),
                 initstate=initstate,
                 states=states,
                 SSE       = final.fit$SSE,
                 sigma2 = sigma2,
                 call      = match.call(),
                 m = m
  ),
  class = "ets"
  )
}

---

###################################################################################
#filter function
A.zzhw <- function(x, lenx, alpha=NULL, beta=NULL, gamma=NULL, seasonal="additive", m,
                 dotrend=FALSE, doseasonal=FALSE, l.start=NULL, exponential = NULL, phi=NULL,
                 b.start=NULL, s.start=NULL)
{
  
  if(exponential!=TRUE || is.null(exponential))
    exponential <- FALSE
  
  if(is.null(phi) || !is.numeric(phi))
    phi <- 1
  
  #initialise array of l, b, s
  level <- trend <- season <- xfit <- residuals <- cbind(numeric(lenx), numeric(lenx)) ######zdvojila som to
  SSE <- 0
  
  if(!dotrend){
    beta <- cbind(c(0,0),c(0,0))
    b.start <- cbind(c(0,0))
  }
  if(!doseasonal){
    gamma <- cbind(c(0,0)) #gamma je tiez matica, alebo vektor?
    s.start[1:length(s.start),] <- ifelse(seasonal=="additive",cbind(c(0,0)),cbind(c(1,1)))   ###A - 0 nahradena c(0,0), 1 nahradena c(1,1), ktovie, ci to je legalne...
  }

  lastlevel <- level0 <- l.start
  lasttrend <- trend0 <- b.start
  season0 <- s.start
  
  for(i in 1:lenx){
    # definel l(t-1)
    if(i>1)
      lastlevel <- level[i-1,]
    #define b(t-1)
    if(i>1)
      lasttrend <- trend[i-1,]
    #define s(t-m)
    if(i>m)
      lastseason <- season[i-m,]
    else
      lastseason <- season0[,i]          ######################################################toto je asi zle! ,i miesto i,
    if(is.na(lastseason[1]))       #######pridala som [1], sice to nerobi, co ma, ale nehubuju warningy
      lastseason <- ifelse(seasonal=="additive",cbind(c(0,0)),cbind(c(1,1)))
    
    #forecast for this period i
    if(seasonal=="additive"){
      if(!exponential)
        xhat <- lastlevel + phi*lasttrend + lastseason
      else
        xhat <- lastlevel * (lasttrend^phi) + lastseason    ###nemam tu maticove nasobenie...
                                ####po novom nechavam maticove nasobenie len pri alpha a beta
    }else {
      if(!exponential)
        xhat <- (lastlevel + phi*lasttrend)*lastseason      ###nemam tu maticove nasobenie...
      else
        xhat <- lastlevel * (lasttrend^phi) * lastseason    ###nemam tu maticove nasobenie...
    }
    
    xfit[i,] <- xhat
###A - ak je to len na vyplutie na konci, tak to nebudem pocitat. bolo by na to treba skor pustit nejaku Distance
#     res <- x[i,] - xhat
#     residuals[i,] <- res
#     SSE <- SSE + res*res
    
    #calculate level[i]
    if(seasonal=="additive"){
      if(!exponential)
        level[i,] <- alpha %*% (x[i,] - lastseason) + (diag(2) - alpha)%*%(lastlevel + phi*lasttrend) ###A
      else
        level[i,] <- alpha %*% (x[i,] - lastseason) + (diag(2) - alpha)%*%(lastlevel * (lasttrend^phi)) ###A
    }
    else {
      if(!exponential)
        level[i,] <- alpha %*% (x[i,] / lastseason) + (diag(2) - alpha)%*%(lastlevel + phi*lasttrend) ###A
      else
        level[i,] <- alpha %*% (x[i,] / lastseason) + (diag(2) - alpha)%*%(lastlevel * (lasttrend^phi)) ###A
    }
    
    #calculate trend[i]
    if(!exponential)
      trend[i,] <- beta%*%(level[i,] - lastlevel) + (diag(2) - beta)%*% (phi*lasttrend)  ###A
    else
      trend[i,] <- beta%*%(level[i,] / lastlevel) + (diag(2) - beta)%*% (lasttrend^phi)  ###A
    
    #calculate season[i]
##############zatial sem dam len taku hroznu blbost:
      season[i,] <- cbind(c(1,1))
#     if(seasonal=="additive"){
#       if(!exponential)
#         season[i,] <- gamma%*%(x[i,] - lastlevel- phi*lasttrend) + (rbind(1,1) - gamma) %*% lastseason ###A
#       else
#         season[i,] <- gamma%*%(x[i,] - lastlevel%*%(lasttrend^phi)) + (rbind(1,1) - gamma) %*% lastseason ###A
#     }else{
#       if(!exponential)
#         season[i,] <- gamma%*%(x[i,]/(lastlevel+phi*lasttrend)) + (rbind(1,1) - gamma) %*% lastseason ###A
#       else
#         season[i,] <- gamma%*%(x[i,]/(lastlevel%*%(lasttrend^phi))) + (rbind(1,1) - gamma) %*% lastseason ###A
#     }
  }
  
  list(SSE=SSE,
       fitted= xfit,
       residuals = residuals,
       level = c(level0,level),
       trend=c(trend0,trend),
       season=c(season0,season),
       phi = phi
  )
}

---

A.initparam <- function(alpha,beta,gamma,phi,trendtype,seasontype,damped,lower,upper,m)
{
  # Set up initial parameters
  par <- numeric(0)
  if(is.null(alpha))
  {
    if(m > 12)
      alpha <- 0.0002
    if(is.null(beta) & is.null(gamma))
      alpha <- lower[1] + .5*(upper[1]-lower[1])
    else if(is.null(gamma))
      alpha <- beta+0.001
    else if(is.null(beta))
      alpha <- 0.999-gamma
    else
      alpha <- 0.5*(beta - gamma + 1)
    if(alpha < lower[1] | alpha > upper[1])
      stop("Inconsistent parameter limits")
    par <- alpha
    names(par) <- "alpha"
  }
  if(is.null(beta))
  {
    if(trendtype !="N")
    {
      if(m > 12)
        beta <- 0.00015
      else
        beta <- lower[2] + .1*(upper[2]-lower[2])
      if(beta > alpha)
        beta <- min(alpha - 0.0001,0.0001)
      if(beta < lower[2] | beta > upper[2])
        stop("Can't find consistent starting parameters")
      par <- c(par,beta)
      names(par)[length(par)] <- "beta"
    }
  }
  if(is.null(gamma))
  {
    if(seasontype !="N")
    {
      if(m > 12)
        gamma <- 0.0002
      else
        gamma <- lower[3] + .01*(upper[3]-lower[3])
      if(gamma > 1-alpha)
        gamma <- min(0.999-alpha,0.001)
      if(gamma < lower[3] | gamma > upper[3])
        stop("Can't find consistent starting parameters")
      par <- c(par,gamma)
      names(par)[length(par)] <- "gamma"
    }
  }
  if(is.null(phi))
  {
    if(damped)
    {
      phi <- lower[4] + .99*(upper[4]-lower[4])
      par <- c(par,phi)
      names(par)[length(par)] <- "phi"
    }
  }
  
  return(par)
}

---

A.holt <- function (x, h = 10, damped = FALSE, level = c(80, 95), fan = FALSE,
                  initial=c("optimal","simple"), exponential=FALSE, alpha=NULL, beta=NULL, ...)
{
  
###A
#   initial <- match.arg(initial)
#   if(initial=="optimal" ) #| damped)
#   {
#     if(exponential) {
#       fcast <- forecast(ets(x, "MMN", alpha=alpha, beta=beta, damped = damped, opt.crit="mse"), h, level = level, fan = fan, ...)
#       stop("prve")
#     }
#     else {
#     fcast <- forecast(ets(x, "AAN", alpha=alpha, beta=beta, damped = damped, opt.crit="mse"), h, level = level, fan = fan, ...)
###A
#     }
#   }	  
#   else {
     fcast <- forecast(A.HoltWintersZZ(x, alpha=alpha, beta=beta, gamma=FALSE, exponential=exponential),
                       h, level = level, fan = fan, ...)
#     stop("vliezlo to sem")
#   }
###A
#   if (damped)
#   {
#     fcast$method <- "Damped Holt's method"
#     if(initial=="heuristic")
#       warning("Damped Holt's method requires optimal initialization")
#   }
#   else
    fcast$method <- "Holt's method"
###A
#   if(exponential)
#     fcast$method <- paste(fcast$method,"with exponential trend")
  fcast$model$call <- match.call()
  return(fcast)
}



---


plot.nnet <- function(mod.in,nid=T,all.out=T,all.in=T,bias=T,wts.only=F,rel.rsc=5,circle.cex=5,
                    node.labs=T,var.labs=T,x.lab=NULL,y.lab=NULL,line.stag=NULL,struct=NULL,cex.val=1,
                    alpha.val=1,circle.col='lightblue',pos.col='black',neg.col='grey', max.sp = F, ...){
  
  require(scales)
  
  #sanity checks
  if('mlp' %in% class(mod.in)) warning('Bias layer not applicable for rsnns object')
  if('numeric' %in% class(mod.in)){
    if(is.null(struct)) stop('Three-element vector required for struct')
    if(length(mod.in) != ((struct[1]*struct[2]+struct[2]*struct[3])+(struct[3]+struct[2])))
      stop('Incorrect length of weight matrix for given network structure')
  }
  if('train' %in% class(mod.in)){
    if('nnet' %in% class(mod.in$finalModel)){
      mod.in<-mod.in$finalModel
      warning('Using best nnet model from train output')
    }
    else stop('Only nnet method can be used with train object')
  }
  
  #gets weights for neural network, output is list
  #if rescaled argument is true, weights are returned but rescaled based on abs value
  nnet.vals<-function(mod.in,nid,rel.rsc,struct.out=struct){
    
    require(scales)
    require(reshape)
    
    if('numeric' %in% class(mod.in)){
      struct.out<-struct
      wts<-mod.in
    }
    
    #neuralnet package
    if('nn' %in% class(mod.in)){
      struct.out<-unlist(lapply(mod.in$weights[[1]],ncol))
    	struct.out<-struct.out[-length(struct.out)]
    	struct.out<-c(
    		length(mod.in$model.list$variables),
    		struct.out,
    		length(mod.in$model.list$response)
    		)    		
      wts<-unlist(mod.in$weights[[1]])   
    }
    
    #nnet package
    if('nnet' %in% class(mod.in)){
      struct.out<-mod.in$n
      wts<-mod.in$wts
    }
    
    #RSNNS package
    if('mlp' %in% class(mod.in)){
      struct.out<-c(mod.in$nInputs,mod.in$archParams$size,mod.in$nOutputs)
      hid.num<-length(struct.out)-2
      wts<-mod.in$snnsObject$getCompleteWeightMatrix()
      
      #get all input-hidden and hidden-hidden wts
      inps<-wts[grep('Input',row.names(wts)),grep('Hidden_2',colnames(wts)),drop=F]
      inps<-melt(rbind(rep(NA,ncol(inps)),inps))$value
      uni.hids<-paste0('Hidden_',1+seq(1,hid.num))
      for(i in 1:length(uni.hids)){
        if(is.na(uni.hids[i+1])) break
        tmp<-wts[grep(uni.hids[i],rownames(wts)),grep(uni.hids[i+1],colnames(wts)),drop=F]
        inps<-c(inps,melt(rbind(rep(NA,ncol(tmp)),tmp))$value)
        }
      
      #get connections from last hidden to output layers
      outs<-wts[grep(paste0('Hidden_',hid.num+1),row.names(wts)),grep('Output',colnames(wts)),drop=F]
      outs<-rbind(rep(NA,ncol(outs)),outs)
      
      #weight vector for all
      wts<-c(inps,melt(outs)$value)
      assign('bias',F,envir=environment(nnet.vals))
      }
    
    if(nid) wts<-rescale(abs(wts),c(1,rel.rsc))
    
    #convert wts to list with appropriate names 
    hid.struct<-struct.out[-c(length(struct.out))]
    row.nms<-NULL
    for(i in 1:length(hid.struct)){
      if(is.na(hid.struct[i+1])) break
      row.nms<-c(row.nms,rep(paste('hidden',i,seq(1:hid.struct[i+1])),each=1+hid.struct[i]))
    }
    row.nms<-c(
      row.nms,
      rep(paste('out',seq(1:struct.out[length(struct.out)])),each=1+struct.out[length(struct.out)-1])
      )
    out.ls<-data.frame(wts,row.nms)
    out.ls$row.nms<-factor(row.nms,levels=unique(row.nms),labels=unique(row.nms))
    out.ls<-split(out.ls$wts,f=out.ls$row.nms)
    
    assign('struct',struct.out,envir=environment(nnet.vals))
    
    out.ls
    
    }
  
  wts<-nnet.vals(mod.in,nid=F)
  
  if(wts.only) return(wts)
  
  #circle colors for input, if desired, must be two-vector list, first vector is for input layer
  if(is.list(circle.col)){
                    circle.col.inp<-circle.col[[1]]
                    circle.col<-circle.col[[2]]
                    }
  else circle.col.inp<-circle.col
  
  #initiate plotting
  x.range<-c(0,100)
  y.range<-c(0,100)
  #these are all proportions from 0-1
  if(is.null(line.stag)) line.stag<-0.011*circle.cex/2
  layer.x<-seq(0.17,0.9,length=length(struct))
  bias.x<-layer.x[-length(layer.x)]+diff(layer.x)/2
  bias.y<-0.95
  circle.cex<-circle.cex
  
  #get variable names from mod.in object
  #change to user input if supplied
  if('numeric' %in% class(mod.in)){
    x.names<-paste0(rep('X',struct[1]),seq(1:struct[1]))
    y.names<-paste0(rep('Y',struct[3]),seq(1:struct[3]))
  }
  if('mlp' %in% class(mod.in)){
    all.names<-mod.in$snnsObject$getUnitDefinitions()
    x.names<-all.names[grep('Input',all.names$unitName),'unitName']
    y.names<-all.names[grep('Output',all.names$unitName),'unitName']
  }
  if('nn' %in% class(mod.in)){
    x.names<-mod.in$model.list$variables
    y.names<-mod.in$model.list$respons
  }
  if('xNames' %in% names(mod.in)){
    x.names<-mod.in$xNames
    y.names<-attr(terms(mod.in),'factor')
    y.names<-row.names(y.names)[!row.names(y.names) %in% x.names]
  }
  if(!'xNames' %in% names(mod.in) & 'nnet' %in% class(mod.in)){
    if(is.null(mod.in$call$formula)){
      x.names<-colnames(eval(mod.in$call$x))
      y.names<-colnames(eval(mod.in$call$y))
    }
    else{
      forms<-eval(mod.in$call$formula)
      x.names<-mod.in$coefnames
      facts<-attr(terms(mod.in),'factors')
      y.check<-mod.in$fitted
      if(ncol(y.check)>1) y.names<-colnames(y.check)
      else y.names<-as.character(forms)[2]
    } 
  }
  #change variables names to user sub 
  if(!is.null(x.lab)){
    if(length(x.names) != length(x.lab)) stop('x.lab length not equal to number of input variables')
    else x.names<-x.lab
  }
  if(!is.null(y.lab)){
    if(length(y.names) != length(y.lab)) stop('y.lab length not equal to number of output variables')
    else y.names<-y.lab
  }
  
  #initiate plot
  plot(x.range,y.range,type='n',axes=F,ylab='',xlab='',...)
  
  #function for getting y locations for input, hidden, output layers
  #input is integer value from 'struct'
  get.ys<-function(lyr, max_space = max.sp){
  	if(max_space){ 
  		spacing <- diff(c(0*diff(y.range),0.9*diff(y.range)))/lyr
   	} else {
    	spacing<-diff(c(0*diff(y.range),0.9*diff(y.range)))/max(struct)
   	}
    
  		seq(0.5*(diff(y.range)+spacing*(lyr-1)),0.5*(diff(y.range)-spacing*(lyr-1)),
        length=lyr)
  }
  
  #function for plotting nodes
  #'layer' specifies which layer, integer from 'struct'
  #'x.loc' indicates x location for layer, integer from 'layer.x'
  #'layer.name' is string indicating text to put in node
  layer.points<-function(layer,x.loc,layer.name,cex=cex.val){
    x<-rep(x.loc*diff(x.range),layer)
    y<-get.ys(layer)
    points(x,y,pch=21,cex=circle.cex,col=in.col,bg=bord.col)
    if(node.labs) text(x,y,paste(layer.name,1:layer,sep=''),cex=cex.val)
    if(layer.name=='I' & var.labs) text(x-line.stag*diff(x.range),y,x.names,pos=2,cex=cex.val)      
    if(layer.name=='O' & var.labs) text(x+line.stag*diff(x.range),y,y.names,pos=4,cex=cex.val)
  }
  
  #function for plotting bias points
  #'bias.x' is vector of values for x locations
  #'bias.y' is vector for y location
  #'layer.name' is  string indicating text to put in node
  bias.points<-function(bias.x,bias.y,layer.name,cex,...){
    for(val in 1:length(bias.x)){
      points(
        diff(x.range)*bias.x[val],
        bias.y*diff(y.range),
        pch=21,col=in.col,bg=bord.col,cex=circle.cex
      )
      if(node.labs)
        text(
          diff(x.range)*bias.x[val],
          bias.y*diff(y.range),
          paste(layer.name,val,sep=''),
          cex=cex.val
        )
    }
  }
  
  #function creates lines colored by direction and width as proportion of magnitude
  #use 'all.in' argument if you want to plot connection lines for only a single input node
  layer.lines<-function(mod.in,h.layer,layer1=1,layer2=2,out.layer=F,nid,rel.rsc,all.in,pos.col,
                        neg.col,...){
    
    x0<-rep(layer.x[layer1]*diff(x.range)+line.stag*diff(x.range),struct[layer1])
    x1<-rep(layer.x[layer2]*diff(x.range)-line.stag*diff(x.range),struct[layer1])
    
    if(out.layer==T){
      
      y0<-get.ys(struct[layer1])
      y1<-rep(get.ys(struct[layer2])[h.layer],struct[layer1])
      src.str<-paste('out',h.layer)
      
      wts<-nnet.vals(mod.in,nid=F,rel.rsc)
      wts<-wts[grep(src.str,names(wts))][[1]][-1]
      wts.rs<-nnet.vals(mod.in,nid=T,rel.rsc)
      wts.rs<-wts.rs[grep(src.str,names(wts.rs))][[1]][-1]
      
      cols<-rep(pos.col,struct[layer1])
      cols[wts<0]<-neg.col
      
      if(nid) segments(x0,y0,x1,y1,col=cols,lwd=wts.rs)
      else segments(x0,y0,x1,y1)
      
    }
    
    else{
      
      if(is.logical(all.in)) all.in<-h.layer
      else all.in<-which(x.names==all.in)
      
      y0<-rep(get.ys(struct[layer1])[all.in],struct[2])
      y1<-get.ys(struct[layer2])
      src.str<-paste('hidden',layer1)
      
      wts<-nnet.vals(mod.in,nid=F,rel.rsc)
      wts<-unlist(lapply(wts[grep(src.str,names(wts))],function(x) x[all.in+1]))
      wts.rs<-nnet.vals(mod.in,nid=T,rel.rsc)
      wts.rs<-unlist(lapply(wts.rs[grep(src.str,names(wts.rs))],function(x) x[all.in+1]))
      
      cols<-rep(pos.col,struct[layer2])
      cols[wts<0]<-neg.col
      
      if(nid) segments(x0,y0,x1,y1,col=cols,lwd=wts.rs)
      else segments(x0,y0,x1,y1)
      
    }
    
  }
  
  bias.lines<-function(bias.x,mod.in,nid,rel.rsc,all.out,pos.col,neg.col,...){
    
    if(is.logical(all.out)) all.out<-1:struct[length(struct)]
    else all.out<-which(y.names==all.out)
    
    for(val in 1:length(bias.x)){
      
      wts<-nnet.vals(mod.in,nid=F,rel.rsc)
      wts.rs<-nnet.vals(mod.in,nid=T,rel.rsc)
      
    	if(val != length(bias.x)){
        wts<-wts[grep('out',names(wts),invert=T)]
        wts.rs<-wts.rs[grep('out',names(wts.rs),invert=T)]
    		sel.val<-grep(val,substr(names(wts.rs),8,8))
    		wts<-wts[sel.val]
    		wts.rs<-wts.rs[sel.val]
    		}
    
    	else{
        wts<-wts[grep('out',names(wts))]
        wts.rs<-wts.rs[grep('out',names(wts.rs))]
      	}
      
      cols<-rep(pos.col,length(wts))
      cols[unlist(lapply(wts,function(x) x[1]))<0]<-neg.col
      wts.rs<-unlist(lapply(wts.rs,function(x) x[1]))
      
      if(nid==F){
        wts.rs<-rep(1,struct[val+1])
        cols<-rep('black',struct[val+1])
      }
      
      if(val != length(bias.x)){
        segments(
          rep(diff(x.range)*bias.x[val]+diff(x.range)*line.stag,struct[val+1]),
          rep(bias.y*diff(y.range),struct[val+1]),
          rep(diff(x.range)*layer.x[val+1]-diff(x.range)*line.stag,struct[val+1]),
          get.ys(struct[val+1]),
          lwd=wts.rs,
          col=cols
        )
      }
      
      else{
        segments(
          rep(diff(x.range)*bias.x[val]+diff(x.range)*line.stag,struct[val+1]),
          rep(bias.y*diff(y.range),struct[val+1]),
          rep(diff(x.range)*layer.x[val+1]-diff(x.range)*line.stag,struct[val+1]),
          get.ys(struct[val+1])[all.out],
          lwd=wts.rs[all.out],
          col=cols[all.out]
        )
      }
      
    }
  }
  
  #use functions to plot connections between layers
  #bias lines
  if(bias) bias.lines(bias.x,mod.in,nid=nid,rel.rsc=rel.rsc,all.out=all.out,pos.col=alpha(pos.col,alpha.val),
                      neg.col=alpha(neg.col,alpha.val))
  
  #layer lines, makes use of arguments to plot all or for individual layers
  #starts with input-hidden
  #uses 'all.in' argument to plot connection lines for all input nodes or a single node
  if(is.logical(all.in)){  
    mapply(
      function(x) layer.lines(mod.in,x,layer1=1,layer2=2,nid=nid,rel.rsc=rel.rsc,
        all.in=all.in,pos.col=alpha(pos.col,alpha.val),neg.col=alpha(neg.col,alpha.val)),
      1:struct[1]
    )
  }
  else{
    node.in<-which(x.names==all.in)
    layer.lines(mod.in,node.in,layer1=1,layer2=2,nid=nid,rel.rsc=rel.rsc,all.in=all.in,
                pos.col=alpha(pos.col,alpha.val),neg.col=alpha(neg.col,alpha.val))
  }
  #connections between hidden layers
  lays<-split(c(1,rep(2:(length(struct)-1),each=2),length(struct)),
              f=rep(1:(length(struct)-1),each=2))
  lays<-lays[-c(1,(length(struct)-1))]
  for(lay in lays){
    for(node in 1:struct[lay[1]]){
      layer.lines(mod.in,node,layer1=lay[1],layer2=lay[2],nid=nid,rel.rsc=rel.rsc,all.in=T,
                  pos.col=alpha(pos.col,alpha.val),neg.col=alpha(neg.col,alpha.val))
    }
  }
  #lines for hidden-output
  #uses 'all.out' argument to plot connection lines for all output nodes or a single node
  if(is.logical(all.out))
    mapply(
      function(x) layer.lines(mod.in,x,layer1=length(struct)-1,layer2=length(struct),out.layer=T,nid=nid,rel.rsc=rel.rsc,
                              all.in=all.in,pos.col=alpha(pos.col,alpha.val),neg.col=alpha(neg.col,alpha.val)),
      1:struct[length(struct)]
      )
  else{
    node.in<-which(y.names==all.out)
    layer.lines(mod.in,node.in,layer1=length(struct)-1,layer2=length(struct),out.layer=T,nid=nid,rel.rsc=rel.rsc,
                pos.col=pos.col,neg.col=neg.col,all.out=all.out)
  }
  
  #use functions to plot nodes
  for(i in 1:length(struct)){
    in.col<-bord.col<-circle.col
    layer.name<-'H'
    if(i==1) { layer.name<-'I'; in.col<-bord.col<-circle.col.inp}
    if(i==length(struct)) layer.name<-'O'
    layer.points(struct[i],layer.x[i],layer.name)
    }

  if(bias) bias.points(bias.x,bias.y,'B')
  
}


---


predict.knn <- function(lag, k, len, data, distance = abs.difference, combination = mean) {
  #TODO incorporate more explanatory variables - nie je take jednoduche, ako som si myslela. zblaznim sa z indexov atd.
  #TODO nejake kontroly na lag, aby som nevybehla z indexov...
  
  #mam data, kde su len zname data, obviously. takze prvu predikciu pre lag=1 predikujem poslednou hodnotou.
  #takze pre lag=1 potrebujem hodnotu data[length(data)], teda data[length(data) - (lag-1)] pre vseobecny lag
  
  all.distances <- list()
  for (i in 1:length(data)) {
    if (i >= len) { #mozem tu saskovat len ak sa mi tam este zmesti chvostik. pre len=1 je to len ta hodnota, pre len=2 este aj jedna dolava od nej, atd.
      dist <- 0
      for (l in 1:len) {
        current.val <- data[length(data) - (lag - 1) - (l - 1)]
        
        data.compared <- data[i - (l - 1)]
        
        dist <- dist + distance(current.val,  data.compared)
      }
      
      all.distances[i] <- dist
      #v all.distances by teraz mali byt distances jednotlivych postupnosti dlzky len od postupnosti pred mojou expl var
    } else {
      all.distances[i] <- NaN
    }
  }
  
  all.distances <- unlist(all.distances)
  
  explained.vals <- list()
  for (i in 1:length(data)) {
    if (i <= (length(data) - lag)) {
      explained.vals[i] <- data[i + lag]
    } else {
      explained.vals[i] <- NaN
    }
  }
  
  explained.vals <- unlist(explained.vals)
  
  all.i.need <- cbind(all.distances, explained.vals)
  all.i.need <- data.frame(all.i.need)
  all.i.need <- all.i.need[is.finite(all.i.need$explained.vals), ]
  sorted <- all.i.need[order(all.distances), ]
  sorted <- sorted[is.finite(sorted$explained.vals), ]
  k.neighbours <- sorted[1:k,]
  k.neighbours.data <- k.neighbours$explained.vals
  predicted.value <- combination(k.neighbours.data)
  
  return (list(oneforecast = predicted.value))
}

---

#TODO otestovat
#TODO najst mean.interval - "priemer" intervalov, je definovany niekde v tych paperoch ci co
predict.knn.interval <- function(lag, k, len, data, distance = euclidean.distance, combination = mean.interval) {
  #TODO incorporate more explanatory variables - nie je take jednoduche, ako som si myslela. zblaznim sa z indexov atd.
  #TODO nejake kontroly na lag, aby som nevybehla z indexov...
  
  all.distances <- list()
  for (i in 1:length(data)) {
    if (i >= len) { #mozem tu saskovat len ak sa mi tam este zmesti chvostik. pre len=1 je to len ta hodnota, pre len=2 este aj jedna dolava od nej, atd.
      dist <- 0
      for (l in 1:len) {
        current.val <- data[length(data) - (lag - 1) - (l - 1), ]  ######
        
        data.compared <- data[i - (l - 1), ] ######
        
        dist <- dist + distance(current.val,  data.compared)
      }
      
      all.distances[i] <- dist
      #v all.distances by teraz mali byt distances jednotlivych postupnosti dlzky len od postupnosti pred mojou expl var
    } else {
      all.distances[i] <- NaN
    }
  }
  
  all.distances <- unlist(all.distances)
  
  explained.vals.centers <- list()
  explained.vals.radii <- list()
  for (i in 1:length(data)) {
    if (i <= (length(data) - lag)) {
      explained.vals.centers[i] <- data[i + lag, ][1]
      explained.vals.radii[i] <- data[i + lag, ][2]
    } else {
      explained.vals.centers[i] <- NaN
      explained.vals.radii[i] <- NaN
    }
  }
  
  explained.vals.centers <- unlist(explained.vals.centers)
  explained.vals.radii <- unlist(explained.vals.radii)
  
  all.i.need <- cbind(all.distances, explained.vals.centers, explained.vals.radii)
  all.i.need <- data.frame(all.i.need)
  all.i.need <- all.i.need[is.finite(all.i.need$explained.vals.centers), ]
  sorted <- all.i.need[order(all.distances), ]
  sorted <- sorted[is.finite(sorted$explained.vals.centers), ]
  k.neighbours <- sorted[1:k,]
  k.neighbours.data.centers <- k.neighbours$explained.vals.centers
  k.neighbours.data.radii <- k.neighbours$explained.vals.radii
  predicted.value <- combination(k.neighbours.data.centers, k.neighbours.data.radii)
  
  return (predicted.value)
}

---

mean.interval <- function(centers, radii) {#TODO najst definiciu mean.intervalu
  cen = mean(centers)
  rad = mean(radii)
  
  return (c(center=cen, radius=rad))
}

---

#TODO pridat nastavitelny parameter beta! pre kazdu dist. takze dodat asi parameter do predict.knn.interval
euclidean.distance <- function(expected, found) {
  beta <- 0.5
  return (beta * (found[1] - expected[1])^2  +  (1-beta) * (found[2] - expected[2])^2)
}

---

abs.difference <- function(first, second) {
  return (abs(first - second))
}
