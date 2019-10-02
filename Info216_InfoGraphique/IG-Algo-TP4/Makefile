DEBUG=yes
UNAME=$(shell uname)
CC=g++
GCC=gcc
CFLAGS=-Wno-parentheses -Wno-deprecated
LDFLAGS=-Wno-parentheses -Wno-deprecated
ifeq ($(UNAME),Darwin)
	LIBS=-lm -lpthread -framework OpenGL -framework GLUT
else
	LIBS=-lm -lpthread -lGL -lglut
endif
ifeq ($(DEBUG),yes)
	CFLAGS+=-g
	LDFLAGS+=-g
endif
BIN=ig
SRC=$(wildcard *.cpp)
HDR=$(wildcard *.hpp)
OBJ=$(SRC:.cpp=.o)

all: $(BIN)

$(BIN): $(OBJ)
	$(CC) -o $@ $^ $(LIBS) $(LDFLAGS)

%.o: %.cpp %.hpp
	$(CC) -c -o $@ $< $(CFLAGS)

main.o: main.cpp $(HDR)
	$(CC) -c -o $@ $< $(CFLAGS)

.PHONY: clean proper

clean:
	rm -f $(OBJ)

proper: clean
	rm -f $(BIN)
