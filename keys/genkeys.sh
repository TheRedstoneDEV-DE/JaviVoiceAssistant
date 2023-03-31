#!/bin/bash

keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 -validity 365 -keystore server-key.jks
keytool -exportcert -alias serverkey -file servercert.crt -keystore server-key.jks
keytool -importcert -alias servercert -file servercert.crt -keystore client-trust.jks
