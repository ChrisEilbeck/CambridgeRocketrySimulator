# use this compile command
LATEX=pdflatex
TARGET=user_guide

all: build clean

build:
	$(LATEX) $(TARGET).tex
	# makeglossaries $(TARGET)
	# bibtex $(TARGET)
	$(LATEX) $(TARGET).tex
	$(LATEX) $(TARGET).tex

clean:
	rm -f *.ist *.acn *.alg *.aux *.bbl *.blg *.glg *.glo *.lof *.acr *.gls *.out *.log *.tdo *.fls *.fdb_latexmk *.bcf *.lot *.toc
