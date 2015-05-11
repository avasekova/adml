#funguje to presne tak isto, ako ked dam fnn::knn.reg trenovacie data s lagom 1
#knn.reg(traindata[1:(length(traindata)-1)], y=traindata[2:length(traindata)], k=k)
#co funguje horsie (aspon na Brentovi) ako ked tomu dam traindata, traindata (vtedy to asi robi
# nejake jak keby MA alebo co)



#vypocitaj distance pre vsetky body od posledneho znameho bodu
#to, co je najpodobnejsie (k najmensich distances) vezmi a pozri sa na ich naslednikov
#spocitaj priemer tychto naslednikov a daj ho ako predikovanu hodnotu pre dany bod
#pre testing data to rob one-step ahead
knn.simple <- function(train, test = NULL, distance.fun, k) {
  
  #first compute the predictions for all the training data:
  pred.train <- rep(NaN, len=length(train))
  
  for (i in 2:length(train)) {
    #prepare the structure: ordernum, trainvalue, distance
    traincols <- cbind(1:length(train), train, rep(0, len=length(train)))
    
    #for all training entries
    for (c in 1:length(train)) {
      #compute the distance (3rd column) from my predecessor
      traincols[c,3] <- distance.fun(train[i-1], train[c])
    }
    
    #now throw out the current value of my predecessor
    #oh noes, don't do this!!!
    ####traincols <- traincols[traincols[,1] != (i-1), ]
    
    #order by the distance and take the first k+2
    #taking k+1 in case one of them is the last one, i.e. no successor to observe
    #and do not take the first one, which is the same entry
    k.neighbours <- traincols[order(traincols[,3]), ][2:(k+2), ]
    k.neighbours <- k.neighbours[k.neighbours[,1] != length(train), ]
    k.neighbours <- k.neighbours[1:k, ]
    
    #now take the successors of the k nearest neighbours and average them into the prediction for i
    successors.of.k <- 1:k
    for (c in 1:k) {
      successors.of.k[c] <- traincols[traincols[,1] == (k.neighbours[c,1]+1), ][2]
    }
    
    pred.train[i] <- mean(successors.of.k)
  }
  
  if (! is.null(test)) { #TODO finish also this part - one-step ahead forecasts
    
  }
  
  pred.train
}
