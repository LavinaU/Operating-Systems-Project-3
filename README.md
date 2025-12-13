# CS4348 Project 3 - BTree Index File

## Description
This project implements a command-line BTree index file manager in Java. 
It supports creating an index file, inserting key value pairs, searching keys, printing the tree, loading from a CSV, and extracting to a CSV.

## Commands
- **create `<filename>`**: Create a new index file.
- **insert `<filename>` `<key>` `<value>`**: Insert a key value pair into the index.
- **search `<filename>` `<key>`**: Search for a key in the index file.
- **print `<filename>`**: Print all key value pairs in order.
- **load `<filename>` `<csv>`**: Load key value pairs from a CSV file into the index.
- **extract `<filename>` `<csv>`**: Extract all key value pairs to a CSV file.

## Usage Examples
```bash
java -cp bin src.Project3 create test.idx
java -cp bin src.Project3 insert test.idx 15 100
java -cp bin src.Project3 search test.idx 15
java -cp bin src.Project3 print test.idx
java -cp bin src.Project3 load test.idx input.csv
java -cp bin src.Project3 extract test.idx output.csv

