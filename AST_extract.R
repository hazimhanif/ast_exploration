

## Creating ASTs from different software packages (C/C++)

i <- 0
j <- 0
package <- dir("src")

for(i in seq(1,length(package))){

  ## Check if ast already exists
  if(dir.exists(paste("ast/",package[i],sep = ""))){
    next
  }else{
    dir.create(paste("ast/",package[i],sep = ""))
  }
  
  ## Recursive find .c/cc files for the specific package
  path_comb = paste("src/",package[i],sep = "")
  filepath = dir(path = path_comb,pattern = "(\\.c$)|(\\.cc$)",recursive = TRUE)
  
  for(j in seq(1,length(filepath))){
    
    ## Create AST
    filename = paste("ast/",package,"/",strsplit(filepath[j],"/")[[1]][-1],".ast",sep = "")
    command <- paste("java -jar codesensor/CodeSensor.jar src/",package[i],"/", filepath[j], " > ",filename,sep = "")
    system(command)
    
  }
}

## Import file into dataframe
mydf <- read.csv("ast/libtiff/tools.ast",
                 header = FALSE,
                 sep = "\t", 
                 col.names = c("type","start","end","depth","value"),
                 stringsAsFactors = FALSE)

