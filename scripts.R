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
  
  return (predicted.value)
}





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


mean.interval <- function(centers, radii) {#TODO najst definiciu mean.intervalu
  cen = mean(centers)
  rad = mean(radii)
  
  return (c(center=cen, radius=rad))
}

#TODO pridat nastavitelny parameter beta! pre kazdu dist. takze dodat asi parameter do predict.knn.interval
euclidean.distance <- function(expected, found) {
  beta <- 0.5
  return (beta * (found[1] - expected[1])^2  +  (1-beta) * (found[2] - expected[2])^2)
}

abs.difference <- function(first, second) {
  return (abs(first - second))
}
