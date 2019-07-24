#!/usr/bin/env bash

echo 'As sorted by `sort`' >AUTHORS.txt
echo >>AUTHORS.txt
git log --format='%aN <%aE>' | sort | uniq >>AUTHORS.txt
