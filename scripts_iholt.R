##NAME
# inv_filter.R
##DATE
# April 16, 2015
##ABOUT
# Compute all states, fitted intervals, metrics
#
#final.fit <- inv.holt.filter(x, lenx = lenx, a = alpha, b = beta,
#                            ul.s = ul.start, ll.s = ll.start,
#                            ut.s = ut.start, lt.s = lt.start,
#                            dotrend = (!is.logical(beta) || beta),
#                            control)

inv.holt.filter <- function(x, lenx, a = NULL, b = NULL,
                           ul.s = NULL, ll.s = NULL, ut.s = NULL, lt.s = NULL,
                           dotrend = FALSE, control = list() ) {
  distance <- control$distance
  we.beta <- control$we.beta
  iy.gama <- control$iy.gama
  #CMNT: In case user set smoothing parameter 'beta' to be 0 or FALSE.
  if(!dotrend) {
    beta <- numeric(4)
    ut.s <- lt.s <- 0
  }
  #CMNT: Define L^(2), T^(2)
  ul.l <- ul.s
  ll.l <- ll.s
  ut.l <- ut.s
  lt.l <- lt.s
  #CMNT: nfi <=> number of fitted intervals; unknown values for time t = 1 and t = 2
  fi <- 3:lenx
  nfi <- length(fi)
  x.ub <- x[,"upperboundary"]
  x.lb <- x[,"lowerboundary"]
  #CMNT: x.upper/lower.average
  x.u.avg <- mean(x.ub[fi])
  x.l.avg <- mean(x.lb[fi])
  #CMNT: Initialize array.
  ul <- ll <- ut <- lt <- uxf <- lxf <- numeric(lenx)
  #ul <- ll <- ut <- lt <- uxf <- lxf <- rep(NA,lenx)
  #CMNT: Initialize metrics.
  mde <- rmse <- mcr <- mer <- u <- arv <- 0
  sum.1 <- sum.2 <- sum.3 <- sum.4 <- sum.5 <- sum.6 <- 0
  #TODO: matrix computation of states
  #a <- matrix(a, nrow = 2, ncol = 2, byrow = FALSE)
  #b <- matrix(b, nrow = 2, ncol = 2, byrow = FALSE)
  
  for(i in fi) {
    #CMNT: I^(t) = L^(t-1) + T^(t-1)
    uxh <- ul.l + ut.l
    lxh <- ll.l + lt.l
    uxf[i] <- uxh
    lxf[i] <- lxh

    x.i.ub <- x.ub[i]
    x.i.lb <- x.lb[i]
    
    upperres <- x.i.ub - uxh
    lowerres <- x.i.lb - lxh    
    #CMNT: x <=> x[i,"upper-/lower-boundary"], y <=> upper-/lower-xfit[i]
    union.xy <- max(x.i.ub, uxh) - min(x.i.lb, lxh)
    #WARN: width of intersection can be negative
    intersect.xy <- min(x.i.ub, uxh) - max(x.i.lb, lxh)
    #WARN: because of previous warning
    if (intersect.xy < 0) {
      union.xy <- union.xy + intersect.xy
      intersect.xy <- 0
    }
    
    width.x <- x.i.ub - x.i.lb
    width.xhat <- uxh - lxh
    dist <- switch(EXPR = distance,
                   H = max(abs(lowerres), abs(upperres)),
                   IY = {union.xy - intersect.xy + iy.gama*(2*intersect.xy - width.x - width.xhat)},
                   DC = {(union.xy - intersect.xy + iy.gama*(2*intersect.xy - width.x - width.xhat)) / union.xy},
                   E = sqrt((lowerres*lowerres + upperres*upperres) / 2),
                   WE = sqrt((we.beta*lowerres*lowerres + (1 - we.beta)*upperres*upperres) / 2),
                   stop("Any distance used. These are available: 'H', 'IY', 'DC', 'E', 'WE'.")
    )
    mde <- mde + dist
    rmse <- rmse + dist^2
    mcr <- mcr + intersect.xy / width.x
    mer <- mer + intersect.xy / width.xhat
    #CMNT: Level/trend equation for upper/lower boundary; [Maia, 2011]
    ul[i] <- ul.i <- a[1]*x.i.ub  +  (1-a[1])*uxh  +  a[3]*(x.i.lb - lxh)
    ll[i] <- ll.i <- a[4]*x.i.lb  +  (1-a[4])*lxh  +  a[2]*(x.i.ub - uxh)
    ut[i] <- ut.i <- b[1]*(ul.i - ul.l)  +  (1 - b[1])*ut.l  +  b[3]*(ll.i - lxh)
    lt[i] <- lt.i <- b[4]*(ll.i - ll.l)  +  (1 - b[4])*lt.l  +  b[2]*(ul.i - uxh)
    #CMNT: Define L^(t-1), T^(t-1)
    ul.l <- ul.i
    ll.l <- ll.i
    ut.l <- ut.i
    lt.l <- lt.i
    
    sum.1 <- sum.1  +  ( x.i.ub - uxh )^2
    sum.2 <- sum.2  +  ( x.i.lb - lxh )^2
    sum.3 <- sum.3  +  ( x.i.ub - x.ub[i-1] )^2
    sum.4 <- sum.4  +  ( x.i.lb - x.lb[i-1] )^2
    sum.5 <- sum.5  +  ( x.i.ub - x.u.avg )^2
    sum.6 <- sum.6  +  ( x.i.lb - x.l.avg )^2
  }
  
  mde <- mde/nfi
  rmse <- sqrt(rmse/nfi)
  mcr <- mcr/nfi
  mer <- mer/nfi
  ref.mod <- sum.1 + sum.2
  u <- sqrt( ref.mod / (sum.3 + sum.4) )
  arv <-     ref.mod / (sum.5 + sum.6)
  
  list(metrics = list(MDE = mde, RMSE = rmse, MCR = mcr, MER = mer, U = u, ARV = arv),
       control = control,
       fitted = c(uxf[fi], lxf[fi]),
       level = c(ul.s, ul[fi], ll.s, ll[fi]),
       trend = c(ut.s, ut[fi], lt.s, lt[fi]) )
}

---

##NAME
# inv_filter_mde.R
##DATE
# April 16, 2015
##ABOUT
# It is called from 'error' in 'optim()' and uses MDE as metric 
# which needs to be MINIMIZED!!!
#
#inv.holt.filter.mde(x, lenx = lenx, a = alpha, b = beta,
#                   ul.s = ul.start, ll.s = ll.start,
#                   ut.s = ut.start, lt.s = lt.start,
#                   dotrend = (!is.logical(beta) || beta),
#                   control)$MDE
#

inv.holt.filter.mde <- function(x, lenx, a = NULL, b = NULL,
                               ul.s = NULL, ll.s = NULL, ut.s = NULL, lt.s = NULL,
                               dotrend = FALSE, control = list() ) {
  distance <- control$distance
  we.beta <- control$we.beta
  iy.gama <- control$iy.gama
  #CMNT: In case user set smoothing parameter 'beta' to be 0 or FALSE.
  if(!dotrend) {
    beta <- numeric(4)
    ut.s <- lt.s <- 0
  }
  #CMNT: Define L^(2), T^(2)
  ul.l <- ul.s
  ll.l <- ll.s
  ut.l <- ut.s
  lt.l <- lt.s
  #CMNT: fi <=> fitted intervals; unknown values for time t = 1 and t = 2
  fi <- 3:lenx
  x.ub <- x[,"upperboundary"]
  x.lb <- x[,"lowerboundary"]
  mde <- 0
  
  for(i in fi) {
    #CMNT: I^(t) = L^(t-1) + T^(t-1)
    uxh <- ul.l + ut.l
    lxh <- ll.l + lt.l
    
    x.i.ub <- x.ub[i]
    x.i.lb <- x.lb[i]
    
    upperres <- x.i.ub - uxh
    lowerres <- x.i.lb - lxh    
    #CMNT: x <=> x[i,"upper-/lower-boundary"], y <=> upper-/lower-xfit[i]
    union.xy <- max(x.i.ub, uxh) - min(x.i.lb, lxh)
    #WARN: width of intersection can be negative
    intersect.xy <- min(x.i.ub, uxh) - max(x.i.lb, lxh)
    #WARN: because of previous warning
    if (intersect.xy < 0) {
      union.xy <- union.xy + intersect.xy
      intersect.xy <- 0
    }
    
    width.x <- x.i.ub - x.i.lb
    width.xhat <- uxh - lxh
    dist <- switch(EXPR = distance,
                   H = max(abs(lowerres), abs(upperres)),
                   IY = {union.xy - intersect.xy + iy.gama*(2*intersect.xy - width.x - width.xhat)},
                   DC = {(union.xy - intersect.xy + iy.gama*(2*intersect.xy - width.x - width.xhat)) / union.xy},
                   E = sqrt((lowerres*lowerres + upperres*upperres) / 2),
                   WE = sqrt((we.beta*lowerres*lowerres + (1 - we.beta)*upperres*upperres) / 2),
                   stop("Any distance used. These are available: 'H', 'IY', 'DC', 'E', 'WE'.")
    )
    mde <- mde + dist
    #CMNT: Level/trend equation for upper/lower boundary; [Maia, 2011]
    ul <- a[1]*x.i.ub  +  (1-a[1])*uxh  +  a[3]*(x.i.lb - lxh)
    ll <- a[4]*x.i.lb  +  (1-a[4])*lxh  +  a[2]*(x.i.ub - uxh)
    ut <- b[1]*(ul - ul.l)  +  (1 - b[1])*ut.l  +  b[3]*(ll - lxh)
    lt <- b[4]*(ll - ll.l)  +  (1 - b[4])*lt.l  +  b[2]*(ul - uxh)
    #CMNT: Define L^(t-1), T^(t-1)
    ul.l <- ul
    ll.l <- ll
    ut.l <- ut
    lt.l <- lt
  }
  
  mde <- mde/length(fi)

  list(MDE = mde)
}

---

##NAME
# inv_filter_u.R
##DATE
# May 18, 2015
##ABOUT
# It is called from 'error' in 'optim()' and uses U/ARV as metric 
# which needs to be MINIMIZED!!!
#
#inv.holt.filter.u(x, lenx = lenx, a = alpha, b = beta,
#                 ul.s = ul.start, ll.s = ll.start,
#                 ut.s = ut.start, lt.s = lt.start,
#                 dotrend = (!is.logical(beta) || beta))$U
#

inv.holt.filter.u <- function(x, lenx, a = NULL, b = NULL,
                             ul.s = NULL, ll.s = NULL, ut.s = NULL, lt.s = NULL,
                             dotrend = FALSE ) {
  #CMNT: In case user set smoothing parameter 'beta' to be 0 or FALSE.
  if(!dotrend) {
    beta <- numeric(4)
    ut.s <- lt.s <- 0
  }
  #CMNT: Define L^(2), T^(2)
  ul.l <- ul.s
  ll.l <- ll.s
  ut.l <- ut.s
  lt.l <- lt.s
  #CMNT: fi <=> fitted intervals; unknown values for time t = 1 and t = 2
  fi <- 3:lenx
  x.ub <- x[,"upperboundary"]
  x.lb <- x[,"lowerboundary"]
  #CMNT: x.upper/lower.average
  x.u.avg <- mean(x.ub[fi])
  x.l.avg <- mean(x.lb[fi])
  sum.1 <- sum.2 <- sum.3 <- sum.4 <- sum.5 <- sum.6 <- 0
  for(i in fi) {
    #CMNT: I^(t) = L^(t-1) + T^(t-1)
    uxh <- ul.l + ut.l
    lxh <- ll.l + lt.l
    
    x.i.ub <- x.ub[i]
    x.i.lb <- x.lb[i]
    #CMNT: Level/trend equation for upper/lower boundary; [Maia, 2011]
    ul <- a[1]*x.i.ub  +  (1-a[1])*uxh  +  a[3]*(x.i.lb - lxh)
    ll <- a[4]*x.i.lb  +  (1-a[4])*lxh  +  a[2]*(x.i.ub - uxh)
    ut <- b[1]*(ul - ul.l)  +  (1 - b[1])*ut.l  +  b[3]*(ll - lxh)
    lt <- b[4]*(ll - ll.l)  +  (1 - b[4])*lt.l  +  b[2]*(ul - uxh)
    #CMNT: Define L^(t-1), T^(t-1)
    ul.l <- ul
    ll.l <- ll
    ut.l <- ut
    lt.l <- lt

    sum.1 <- sum.1  +  ( x.i.ub - uxh )^2
    sum.2 <- sum.2  +  ( x.i.lb - lxh )^2
    sum.3 <- sum.3  +  ( x.i.ub - x.ub[i-1] )^2
    sum.4 <- sum.4  +  ( x.i.lb - x.lb[i-1] )^2
    sum.5 <- sum.5  +  ( x.i.ub - x.u.avg )^2
    sum.6 <- sum.6  +  ( x.i.lb - x.l.avg )^2
  }
  
  ref.mod <- sum.1 + sum.2
  u <- sqrt( ref.mod / (sum.3 + sum.4) )
  arv <-     ref.mod / (sum.5 + sum.6)
  
  list(U = u, ARV = arv)
}

---

##NAME
# inv_holt.R
##DATE
# May 18, 2015
#==============
## Literature
#--------------
# [Maia,2011] MAIA, Andre Luis Santiago and Francisco de A. T. DE CARVALHO. Holt's
#             exponential smoothing and neural network models for forecasting
#             interval-valued time series. International Journal of Forecasting,
#             2011, roc. 27, c. 3, s. 740-759
#==============
## Legend
#--------------
# CMNT <=> COMMENT
#==============
## Parametres
#--------------
# x - time-series object, class = "mts";
#   - can comprise of 3 columns: year, lowerboundary, upperboundary
#   - can comprise of 2 columns:       lowerboundary, upperboundary
# h - number of predictions
# alpha - smoothing parameter of the level;
#       - comprises of 4 values as:
#         - matrix 2x2
#              alpha <- matrix(c(a, b, c, d), nrow = 2, ncol = 2, byrow = FALSE)
#         - vector
#              alpha <- c(a, b, c, d)
# beta - smoothing parameter of the trend;
#      - comprises of 4 values as: matrix 2x2 or vector
# initial - initial values of ul, ll, ut, lt are: set default (simple), computed (optimal)
#==============

inv.holt <- function(x, h = 10, alpha = NULL, beta = NULL,
                    initial = c("simple", "optimal"), control = list(), ...) {
  #CMNT: set 'distance', 'we.beta', 'iy.gama'. (important for 'inv.holt.filter()')
  dist.choices <- c("H", "IY", "DC", "E", "WE")
  ctrl <- list(distance = dist.choices[4],
               we.beta = 0.5,
               iy.gama = 0.5)
  ctrl.dist.len <- length(ctrl$distance)
  ctrl.names <- names(ctrl)
  ctrl[(nms.c <- names(control))] <- control
  if(length(noNms <- nms.c[!nms.c %in% ctrl.names])) 
    warning("Unknown names in 'control': ", paste(noNms, collapse = ", "), 
            ".\n Valid are: 'distance', 'we.beta', 'iy.gama'.")
  if( (length(ctrl$distance) != ctrl.dist.len) || !is.element(ctrl$distance, dist.choices) )
    stop("'distance' must be one of: ", paste(dist.choices, collapse = ", "), ".")
  if( !is.numeric(ctrl$we.beta) || (ctrl$we.beta < 0) || (ctrl$we.beta > 1) )
    stop("'we.beta' must be in interval <0;1>.")
  if( !is.numeric(ctrl$iy.gama) || (ctrl$iy.gama < 0) || (ctrl$iy.gama > 1) )
    stop("'iy.gama' must be in interval <0;1>.")
  control <- ctrl
  #print(ctrl)
  
  initial <- match.arg(initial)
  if (initial == "simple") {
    fcast <- inv.holt.forecast(inv.holt.zz(x, alpha = alpha, beta = beta, control), h, ...)
  } else {
    stop("Not implemented.")
  }
  
  fcast$method <- "The interval variant of Holt's linear trend method"
  fcast$model$call <- match.call()
  return(fcast)
}

---

inv.holt.zz <- function(x, alpha = NULL, beta = NULL, control) {
  x <- as.ts(x)
  tspx <- tsp(x)
  m <- frequency(x)
  lenx <- tspx[2] - tspx[1] + 1

  if (!is.null(alpha) && !is.numeric(alpha))
    stop("Invalid value for smoothing paramter alpha.")
  #CMNT: Variables different from 'NULL' value are checked if they fit within interval.
  if (!all(c(is.null(alpha), is.null(beta))) && any(c(alpha, beta) < 0 | c(alpha, beta) > 1))
    stop("'alpha' and 'beta' must be within the unit interval.")
  #CMNT: The starting values for (upper/lower) level.
  ul.start <- x[2L,"upperboundary"]
  ll.start <- x[2L,"lowerboundary"]

  if (is.null(beta) || !is.logical(beta) || beta) {
    #CMNT: The starting values for (upper/lower) trend.
    ut.start <- x[2L,"upperboundary"] - x[1L,"upperboundary"]
    lt.start <- x[2L,"lowerboundary"] - x[1L,"lowerboundary"]
  }

  #CMNT: The elements of the smoothing parameter matrices 'alpha' and 'beta' are constrained to the range <0;1>
  lower <- rep(0,8)
  upper <- rep(1,8)
  
  #CMNT: Optimization (optim.start, error, select, if)
  #CMNT: Setting initial values for smoothing parameters. (They are required by function 'optim()'.)
  optim.start <- inv.holt.init.param(alpha = alpha, beta = beta, lower = lower, upper = upper, m = m)
  #CMNT: Function to be minimized. (Required by function 'optim()'.)
  error <- function(p, select) {
    if (select[1] > 0) {
      alpha <- p[1:4]
    }
      
    if(select[2] > 0)
      beta <- p[(ifelse(select[1] > 0, 5, 1)):(ifelse(select[1] > 0, 8, 4))]
    
    #CMNT: see the process of computing values of 'alpha', 'beta'
    #cat("a ",alpha, "\t \t \t b ", beta,"\n")
    
    inv.holt.filter.mde(x, lenx = lenx, a = alpha, b = beta,
                       ul.s = ul.start, ll.s = ll.start,
                       ut.s = ut.start, lt.s = lt.start,
                       dotrend = (!is.logical(beta) || beta),
                       control)$MDE

    #inv.holt.filter.u(x, lenx = lenx, a = alpha, b = beta,
    #                 ul.s = ul.start, ll.s = ll.start,
    #                 ut.s = ut.start, lt.s = lt.start,
    #                 dotrend = (!is.logical(beta) || beta))$U
  }
  #CMNT: value 1 means the smoothing parameter need to be optimized, 0 otherwise
  select <- as.numeric(c(is.null(alpha), is.null(beta)))

  if (sum(select) > 0) {
    low <- upp <- numeric(0)
    switch(EXPR = sum(select),
           { low <- ifelse(select[1] > 0, lower[1:4], lower[5:8])
             upp <- ifelse(select[1] > 0, upper[1:4], upper[5:8]) },
           { low <- lower[1:8]
             upp <- upper[1:8] }
           )
    sol <- optim(optim.start, error, method = "L-BFGS-B", lower = low, upper = upp, select = select)
    
    #sol <- optim(optim.start, error, method = "L-BFGS-B",
    #             lower = lower[rep(1, sum(select)*4)], upper = upper[rep(1, sum(select)*4)], select = select)
    
    if(sol$convergence || any(sol$par < 0 | sol$par > 1)) {
      if (sol$convergence > 50) {
        warning(gettextf("Optimization difficulties: %s", sol$message), domain = NA)
      } else {
        stop("Optimization failure.")
      }
    }

    if(select[1] > 0) {
      alpha <- sol$p[1:4]
    }

    if(select[2] > 0) {
      beta <- sol$p[(ifelse(select[1] > 0, 5, 1)):(ifelse(select[1] > 0, 8, 4))]
    }
  }

  final.fit <- inv.holt.filter(x, lenx = lenx, a = alpha, b = beta,
                              ul.s = ul.start, ll.s = ll.start,
                              ut.s = ut.start, lt.s = lt.start,
                              dotrend = (!is.logical(beta) || beta),
                              control)

  names(final.fit$level) <- names(final.fit$trend) <- NULL
  names(final.fit$metrics$MDE) <- names(final.fit$metrics$RMSE) <- NULL
  names(final.fit$metrics$MCR) <- names(final.fit$metrics$MER) <- NULL
  names(final.fit$metrics$U) <- names(final.fit$metrics$ARV) <- NULL
  
  fittedmatrix <- matrix(final.fit$fitted, ncol = 2, byrow = FALSE)
  colnames(fittedmatrix) <- c("uy^", "ly^")
  #CMNT: Since the prediction starts when time t = 3. (start = tspx[1] + 2)
  fitted <- ts(fittedmatrix, frequency = m, start = tspx[1] + 2)
  
  stateslevel <- matrix(final.fit$level, ncol = 2, byrow = FALSE)
  colnames(stateslevel) <- c("ul", "ll")
  statestrend <- matrix(final.fit$trend, ncol = 2, byrow = FALSE)
  colnames(statestrend) <- c("ut", "lt")
  states <- cbind(stateslevel, statestrend)
  #CMNT: Since the initial values are set when time t = 2. (start = tspx[1] + 1/m)
  states <- ts(states, frequency = m, start = tspx[1] + 1/m)
  
  initstate <- states[1,]
  param <- c(alpha, beta)
  names(param) <- c("alpha[1,1]", "alpha[2,1]", "alpha[1,2]", "alpha[2,2]",
                    "beta[1,1]", "beta[2,1]", "beta[1,2]", "beta[2,2]")

  structure(list(x = x,
                 m = m,
                 lenx = lenx,
                 par = c(param, initstate),
                 states = states,
                 fitted = fitted,
                 metrics = final.fit$metrics,
                 control = final.fit$control,
                 call = match.call() ))
}

---

inv.holt.forecast <- function(object, h = ifelse(object$m > 1, 2*object$m, 10), ...) {
  n <- object$lenx
  start.f <- tsp(object$x)[2] + 1/object$m
  #CMNT: Since we do not have states for time t=1. (object$states[n-1,])
  f <- inv.holt.predict(h, object$states[n-1,])
  out <- list(model = object,
              mean = ts(f$prediction, start = start.f, frequency = object$m),
              method = object$method
  )
  return(structure(out))
}

---

inv.holt.predict <- function(h, last.state) {
  prediction <- matrix(numeric(h*2), nrow = h, ncol = 2)
  colnames(prediction) <- c("uy^_h", "ly^_h")
  
  for(i in 1:h) {
    prediction[i,1] <- last.state["ul"] + i*last.state["ut"]
    prediction[i,2] <- last.state["ll"] + i*last.state["lt"]
  }
  
  return(list(prediction = prediction))
}

---

inv.holt.init.param <- function(alpha, beta, lower, upper, m) {
  par <- numeric(0)
  
  if(is.null(alpha)) {
    if(m > 12) {
      alpha <- rep(0.0002, 4)
    } else {
      ##alpha <- c(0.5, 0.1, 0.1, 0.5)
      #alpha <- lower[1] + 0.5*(upper[1] - lower[1])
      #alpha[2] <- lower[2] + 0.5*(upper[2] - lower[2])
      #alpha[3] <- lower[3] + 0.5*(upper[3] - lower[3])
      #alpha[4] <- lower[4] + 0.5*(upper[4] - lower[4])
      alpha <- numeric(0)
      for(i in 1:4) {
        alpha[i] <- lower[i] + 0.5*(upper[i] - lower[i])
        #alpha[i] <- lower[i] + 0*(upper[i] - lower[i])
      }
    }
    if(any(alpha < lower[1:4]) | any(alpha > upper[1:4])) {
      stop("Inconsistent parameter limits.")
    }
    par <- alpha
    #names(par) <- c("a[1,1]", "a[2,1]", "a[1,2]", "a[2,2]")
  }
  
  if(is.null(beta)) {
    if(m > 12) {
      beta <- rep(0.00015, 4)
    } else {
      #beta <- c(0.5, 0.1, 0.1, 0.5)
      beta <- numeric(0)
      for(i in 5:8) {
        beta[i-4] <- lower[i] + 0.1*(upper[i] - lower[i])
        #beta[i-4] <- lower[i] + 0*(upper[i] - lower[i])
      }
    }
    if(sum(as.numeric(beta > alpha)) > 0) {
      for(i in 1:4) {
        if (beta[i] > alpha[i])
          beta[i] <- min(alpha[i] - 0.0001, 0.0001)
      }
    }
    if(any(beta < lower[5:8]) | any(beta > upper[5:8])) {
      stop("Can't find consistent starting parameters.")
    }
    #tmp <- length(par) + 1
    par <- c(par, beta)
    #names(par)[tmp:length(par)] <- c("b[1,1]", "b[2,1]", "b[1,2]", "b[2,2]")
  }
  
  return(par)
}

---

##NAME
# inv_stats.R
##DATE
# April 16, 2015
##ABOUT
# U^I    <=> the interval U of Theil's statistics
# ARV^I  <=> the interval average relative variance
# Compute the accuracy measures U^I and ARV^I on:
#   a) training set
#   b) test set with a forecast horizon of 5 steps ahead
#   c) test set with a forecast horizon of 10 steps ahead
##MODIFIED May 15, 2015 #MDF:

inv.holt.stats <- function(x = NULL, alpha = NULL, beta = NULL) {
  if(any(is.null(x), is.null(alpha), is.null(beta)))
    stop("'ITS', 'alpha', 'beta' must be inserted.")
  a <- alpha
  b <- beta
  
  x <- as.ts(x)
  if((lenx <- tsp(x)[2] - tsp(x)[1] + 1) < 40)
    stop("Not enough observation in 'ITS'. At least 40 obs. is needed.")
  w <- lenx - 39   #size of the sliding window
  tau <- lenx - 38 #start of the testing period
  
  trng <- inv.holt.stats.training(x, a, b, w, tau)
  test <- inv.holt.stats.test(x, tau, trng)
  
  return(structure(list(U       = trng$U,
                        U.h5    = test$U.h5,
                        U.h10   = test$U.h10,
                        ARV     = trng$ARV,
                        ARV.h5  = test$ARV.h5,
                        ARV.h10 = test$ARV.h10 )))
}

---

inv.holt.stats.training <- function(x, a, b, w, tau) {
  r.u <- r.arv <- numeric(30) #training set results
  tr.ul.l <- tr.ll.l <- tr.ut.l <- tr.lt.l <- numeric(30) #last states of each iteration
  
  for(j in 1:30) {
    #MDF:
    #y <- window(x, start = (tau - w), end = (tau - 1), frequency = 1)
    y <- window(x, start = (tau - w + tsp(x)[1] - 1),
                end = (tau - 1 + tsp(x)[1] - 1), frequency = 1)
    
    y.2L.ub <- y[2L, "upperboundary"]
    y.2L.lb <- y[2L, "lowerboundary"]
    ul.l <- y.2L.ub
    ll.l <- y.2L.lb
    ut.l <- y.2L.ub - y[1L, "upperboundary"]
    lt.l <- y.2L.lb - y[1L, "lowerboundary"]
    
    uxf <- lxf <- ul <- ll <- ut <- lt <- 0
    sum.1 <- sum.2 <- sum.3 <- sum.4 <- sum.5 <- sum.6 <- 0
    
    fi <- 3:w #fitted intervals
    #sai - sample average interval
    uy.sai <- mean(y[fi,"upperboundary"]) #upper boundary average
    ly.sai <- mean(y[fi,"lowerboundary"]) #lower boundary average
    for(i in fi) {
      uxf <- ul.l + ut.l
      lxf <- ll.l + lt.l
      y.i.ub <- y[i,"upperboundary"]
      y.i.lb <- y[i,"lowerboundary"]
      ul <- a[1]*y.i.ub  +  (1-a[1])*uxf  +  a[3]*(y.i.lb-lxf)
      ll <- a[4]*y.i.lb  +  (1-a[4])*lxf  +  a[2]*(y.i.ub-uxf)
      ut <- b[1]*(ul-ul.l)  +  (1-b[1])*ut.l  +  b[3]*(ll-lxf)
      lt <- b[4]*(ll-ll.l)  +  (1-b[4])*lt.l  +  b[2]*(ul-uxf)
      sum.1 <- sum.1  +  ( y.i.ub - uxf )^2
      sum.2 <- sum.2  +  ( y.i.lb - lxf )^2
      sum.3 <- sum.3  +  ( y.i.ub - y[i-1,"upperboundary"] )^2
      sum.4 <- sum.4  +  ( y.i.lb - y[i-1,"lowerboundary"] )^2
      sum.5 <- sum.5  +  ( y.i.ub - uy.sai )^2
      sum.6 <- sum.6  +  ( y.i.lb - ly.sai )^2
      ul.l <- ul
      ll.l <- ll
      ut.l <- ut
      lt.l <- lt
    }
    ref.mod <- sum.1 + sum.2
    r.u[j] <- sqrt( ref.mod / (sum.3 + sum.4) )
    r.arv[j] <- ref.mod / (sum.5 + sum.6)
    
    tr.ul.l[j] <- ul.l
    tr.ll.l[j] <- ll.l
    tr.ut.l[j] <- ut.l
    tr.lt.l[j] <- lt.l
    tau <- tau + 1
  }
  
  list(U = mean(r.u),
       ARV = mean(r.arv),
       tr.ul.l = tr.ul.l,
       tr.ll.l = tr.ll.l,
       tr.ut.l = tr.ut.l,
       tr.lt.l = tr.lt.l)
}

---

inv.holt.stats.test <- function(x, tau, trng) {
  ul.l <- trng$tr.ul.l
  ll.l <- trng$tr.ll.l
  ut.l <- trng$tr.ut.l
  lt.l <- trng$tr.lt.l
  
  h <- c(5, 10)
  h.len <- length(h)
  r.u.h <- r.arv.h <- numeric(h.len*30) #test set results
  
  for(j in 1:30) {
    for(k in 1:h.len) {
      scale <- c(tau:(tau+h[k]-1))
      ux.sai <- mean(x[scale,"upperboundary"])
      lx.sai <- mean(x[scale,"lowerboundary"])
      
      sum.1 <- sum.2 <- sum.3 <- sum.4 <- sum.5 <- sum.6 <- 0
      for(i in 1:h[k]) {
        ind <- tau+i-1
        x.ind.ub <- x[ind,"upperboundary"]
        x.ind.lb <- x[ind,"lowerboundary"]
        sum.1 <- sum.1  +  ( x.ind.ub - (ul.l[j] + i*ut.l[j]) )^2
        sum.2 <- sum.2  +  ( x.ind.lb - (ll.l[j] + i*lt.l[j]) )^2
        sum.3 <- sum.3  +  ( x.ind.ub - x[ind-1,"upperboundary"] )^2
        sum.4 <- sum.4  +  ( x.ind.lb - x[ind-1,"lowerboundary"] )^2
        sum.5 <- sum.5  +  ( x.ind.ub - ux.sai )^2
        sum.6 <- sum.6  +  ( x.ind.lb - lx.sai )^2
      }
      h.ref.mod <- sum.1 + sum.2      
      n30 <- j + (k-1)*30
      r.u.h[n30] <- sqrt( h.ref.mod / (sum.3 + sum.4) )
      r.arv.h[n30] <- h.ref.mod / (sum.5 + sum.6)
    }  
    
    tau <- tau + 1
  }
  
  list(U.h5 = mean(r.u.h[c(1:30)]),
       U.h10 = mean(r.u.h[c(31:60)]),
       ARV.h5 = mean(r.arv.h[c(1:30)]),
       ARV.h10 = mean(r.arv.h[c(31:60)]) )
}
