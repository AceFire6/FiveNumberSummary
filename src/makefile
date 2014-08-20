JFLAGS = -g
JC = javac
MAIN_CLASS = FnsDriver
NAME = FiveNumberSummary
.SUFFIXES: .java .class
.java.class:
	@echo Compiling java classes.;
	@$(JC) $(JFLAGS) $*.java;

CLASSES = \
	FnsDriver.java \
	FnsGui.java \
	FnsUtilities.java \
	
default: classes

classes: $(CLASSES:.java=.class)

jar: classes
	@echo Creating MANIFEST.MF;
	@touch MANIFEST.MF;
	@echo Main-Class: $(MAIN_CLASS) > MANIFEST.MF;
	@echo Creating $(NAME).jar;
	@jar cvfm $(NAME).jar MANIFEST.MF $(MAIN_CLASS).class *.class;
	
	@if [ ! -d ../jar/ ]; then\
		echo Making directory ../jar/;\
		mkdir ../jar/;\
	fi
	@echo Moving $(NAME).jar to ../jar/
	@mv $(NAME).jar ../jar/

run: classes
	@echo Running $(MAIN_CLASS)...;
	@java $(MAIN_CLASS)
	@echo Closing...;

run-jar:
	@if [ -f ../jar/$(NAME).jar ]; then\
		java -jar ../jar/$(NAME).jar;\
	else\
		echo You must run make jar first.;\
	fi

clean-all: clean clean-jar

clean:
	@echo Cleaning class files.
	@$(RM) *.class
	
clean-jar:
	@if [ -f MANIFEST.MF ]; then\
		echo Removing created manifest file.;\
		rm MANIFEST.MF;\
	fi

