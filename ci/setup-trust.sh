#!/bin/bash
set -ex

wget -q -O - https://dist.eugridpma.info/distribution/igtf/current/GPG-KEY-EUGridPMA-RPM-3 | apt-key add -

echo "deb http://repository.egi.eu/sw/production/cas/1/current egi-igtf core" >> /etc/apt/sources.list

apt-get update
apt-get install ca-policy-egi-core fetch-crl

fetch-crl -v -T 5
