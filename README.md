# adml

A tool for the analysis of classical and interval time series. Still very much a work in progress.

## Requirements

- R with the following packages and their dependencies: rJava, forecast, JavaGD, XLConnect, FNN, kknn, nnet, scales, reshape, RSNNS, vars, psych, nortest, tseries, bfast, LearnBayes, brnn, FactoMineR

`install.packages(c("rJava", "forecast", "JavaGD", "XLConnect", "FNN", "kknn", "nnet", "scales", "reshape", "RSNNS", "vars", "psych", "nortest", "tseries", "bfast", "LearnBayes", "brnn", "FactoMineR"), dependencies=TRUE)`

- [Windows:] JRI requires `R_HOME`, `R.dll`, `jri.dll`, and `jvm.dll` on `Path`.


## Usage

The simplest way to run the tool is using the `start-main-with-workers` script.

Start by loading a file: `File` - `Load`. For now, `.xls`, `.xlsx` and `.csv` files are supported. The files are
expected to contain one or several columns, each interpreted as a single classical time series. (For Excel spreadsheets, only the first sheet is taken into account.)





#

###### Note

I am not a UX person. All improvements to the GUI are welcome.

> Like all Vogon ships, it looked as if it had been not so much
designed, as congealed. The unpleasant yellow lumps and edifices
which protruded from it at unsightly angles would have
disfigured the looks of most ships, but in this case, that
was sadly impossible. Uglier things have been spotted in the
skies, but not by reliable witnesses.

> *Douglas Adams, The Restaurant at the End of the Universe*
