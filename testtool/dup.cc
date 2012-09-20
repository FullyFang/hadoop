#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/time.h>
#include <string>
#include <set>

using namespace std;

int main()
{	
	//test();
	set<string> s;
	for(int i=0; i<100; i++){
		char filename[30];
		memset(filename, 0, sizeof(filename));
		sprintf(filename, "./output/cdr%02d.txt", i);
		printf("%s\n", filename);
		
		FILE *f = fopen(filename, "r");
		
		char buf[100];
		memset(buf, 0, sizeof(buf));
		while(fgets(buf, sizeof(buf), f) != NULL){
			string t = buf;
			s.insert(t);
			memset(buf, 0, sizeof(buf));
		}

		fclose(f);


	}
	printf("size = %d\n", s.size());
	return 0;
}
