#!/bin/bash

# Update hosts file
echo "[TASK 0] Update /etc/hosts file"
cat >>/etc/hosts<<EOF
172.42.42.220 safewaterfall.cams7.com.br safewaterfall
EOF
