#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/time.h>
#include <string>

using namespace std;

int getrand(int n)
{
	struct timeval tpstart;
	gettimeofday(&tpstart,NULL);
	srand(tpstart.tv_usec);
	return rand() % n;
}

string getrandStr(int len, int mod, bool plus=false)
{
	int tmp = getrand(mod);
	if(plus)
		tmp++;
	char buf[21];
	memset(buf, 0, sizeof(buf));
	sprintf(buf, "%020d", tmp);
	string s = buf+20-len;
	return s;
}

void getMsisdn(string &msisdn, string &areacode)
{
	string t = getrandStr(4, 10000);
	msisdn = "1390000" + t;

	areacode = "010";

	int n = atoi(t.c_str());
	if(n%3 == 1)
		areacode = "021";
	if(n%3 == 2)
		areacode = "020";
}

string getTime()
{
	string t = "201208";
	string day = getrandStr(2, 31, true);
	string h   = getrandStr(2, 24);
	string m   = getrandStr(2, 60);
	string s   = getrandStr(2, 60);
	t += day + h + m + s;
	return t;
}


string getOneCdr()
{
	string oaddr, ocode, daddr, dcode, time;
	getMsisdn(oaddr, ocode);
	getMsisdn(daddr, dcode);
	time = getTime();
	
	string s = oaddr + "," + ocode + "," + daddr + "," + dcode + "," + time + "\n";
	return s;
}

void test()
{
	for(int i=0; i<100; i++)
	{
		string a, b;
		//getMsisdn(a, b);
		//printf("%s\t%s\n", a.c_str(), b.c_str());
		//a = getTime();
		//printf("%s\n", a.c_str());
		//a = getOneCdr();
		//printf("%s\n", a.c_str());
	}
}
int main()
{	
	//test();
	for(int i=0; i<100; i++){
		char filename[30];
		memset(filename, 0, sizeof(filename));
		sprintf(filename, "./output/cdr%02d.txt", i);
		printf("%s\n", filename);
		
		FILE *f = fopen(filename, "w+");

		for(int j=0; j<10000; j++){
			string s = getOneCdr();
			fputs(s.c_str(), f);
		}
		fclose(f);
	}
	return 0;
}
