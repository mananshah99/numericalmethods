#include <stdio.h>
#include <math.h>
#include <assert.h>
#include <string.h>
#include <ctype.h>

typedef enum {
   JAVA,
   CPP,
   C
} source_type_t;

void stripcomments(char* s);

int main(int argc, char* argv[])
{
   assert(argc > 1);

   source_type_t type = JAVA;
   if(argc >= 2) {
      for(int i=0; i<argc; i++) {
         if(strcmp(argv[i], "--java") == 0 || strcmp(argv[i], "-j") == 0) {
            type = JAVA;
         }
         else if(strcmp(argv[i], "--cplusplus") == 0 || strcmp(argv[i], "-cpp") == 0) {
            type = CPP;
         }
         else if(strcmp(argv[i], "--clang") == 0|| strcmp(argv[i], "-c") == 0) {
            type = C;
         }
      }
	}

   char const* const name = argv[1];
   const char OPEN_BRACE = '{';
   const char EQUAL = '=';

   const char* TAB_SPACE = "\t\b\b\b\b\b";

   FILE* file = fopen(name, "r");
   char line[256];
   char* pch;

   // switch(type) { case C: { break } case CPP: { break } case JAVA: { break } }
   int num = 1;
   while (fgets(line, sizeof(line), file))
   {
      stripcomments(line);
      // printf("checking line %d => %s", num, line);

      int contains_alpha = 0;

      for (int i = 0; line[i] != '\0'; i++)
      {
         //printf("LINE %d: Checking character %c and it is %d\n", num, line[i], isalpha(line[i]));
         if (isalpha(line[i])) {
            contains_alpha = 2;
         }
         if(line[i] == EQUAL)
         {
            switch(line[i - 1]) {
               case ' ':
               case '<':
               case '>':
               case '=':
               case '!':
               case '/':
               case '+':
               case '*':
                  break;
               default:
                  if(line[i + 1] != ' ' && line[i + 1] != '=') {
                     printf("[lint.c] line: %d,%schar: %d => squashed equals sign\n", num, TAB_SPACE, i);
                  }
            }
         }
      }

      if(contains_alpha)
      {
         pch = strchr(line, OPEN_BRACE);
         while (pch != NULL)
         {
            printf("[lint.c] line: %d,%schar: %d => misplaced brace\n", num, TAB_SPACE, pch-line+1);
            pch = strchr(pch+1, OPEN_BRACE);
         }
      }
      ++num;
   }
   return 0;
}

const char *ca = "/*", *cb = "*/";
int al = 2, bl = 2;

void stripcomments(char *s)
{
   char *a, *b;
   int len = strlen(s) + 1;

   while ((a = strstr(s, ca)) != NULL)
   {
      b = strstr(a + al, cb);
      if (b == NULL)
         break;
      b += bl;
      memmove(a, b, len - (b - a));
   }
}
