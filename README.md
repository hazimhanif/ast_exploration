
# Experiment 2-Abstract Syntax Trees (AST)

This repo is for the exploration of ASTs from different open source software packages.

## Parser  
* The main parser is [CodeSensor](https://github.com/fabsx00/codesensor) from Fabian Yamaguchi which he implemented in [Yamaguchi et. al. (2012)](https://dl.acm.org/citation.cfm?id=2421003)
* The parser is based on Antlr 3.4 with **Island grammar** implementation as explained in [L. Moonen (2001)](https://ieeexplore.ieee.org/document/957806)
* Currently the parser only understand C/C++ source codes.
* We also modified the CodeSensor into two parser:
	* CodeSensor-Complete
		* Creates full AST from the CodeSensor to follow the structures of ASTNN proposed in https://dl.acm.org/doi/10.1109/ICSE.2019.00086 and 
	* CodeSensor-Minimal
		* Creates AST that try to mimic the output of `Pycparser`, that was used in the ASTNN paper.

## Notebook

There are 2 jupyterlab notebooks:
* `getAST_CodeSensor_complete` is about processing the output from the CodeSensor-Complete parser
* `getAST_CodeSensor_minimal` is about processing the output from the CodeSensor-Minimal parser
* `Exploration_codeclassify`is about the exploration of the ASTs produced and processed by `Pycparser` in the ASTNN paper.

