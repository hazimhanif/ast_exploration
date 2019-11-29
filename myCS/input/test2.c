int main(){
  
	int x=22,y=44;

	if(x==23){
		x=2;
	}else{
		y=314;
	}


	while( x < 21 ) {
	  printf("value of x: %d\n", x);
	  x++;
	}


	x=x+x;
	printf("%d",x);

	return 0;
}
