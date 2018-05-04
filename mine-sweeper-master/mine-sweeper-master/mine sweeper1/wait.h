#ifndef WAIT_H_INCLUDED
#define WAIT_H_INCLUDED

void wait ( float sec ){
		clock_t end_wait;
		end_wait = clock () + sec * CLK_TCK ;

		while (clock() < end_wait) {}
	}


#endif // WAIT_H_INCLUDED
