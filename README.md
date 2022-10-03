# ArduinoRelay
[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)
- Gathering data from Arduino
- Send command to Arduino
- Immediately logic judges and reaction
- Prometheus supported

## Usage
- get command

`/getAll`
return Arduino status in string form, six analog values, and one pin bitmap.

`/setPin/{pin}/{value}`
set a pin into value, note that this is for digital output, the value only can be 0 or 1. Pins 0, 1, 4, 10, and above are occupied by the ethernet shell, and cannot use.

`/setPinPWM/{pin}/{value}`
set a pin into value, note that this is for PWM output, the value only can be 0 to 255. Only pins 3, 5, 6, and 9 are functional.

## Communication Formate
The server will query Arduino every 250ms, it will return 16 bytes which are:
|Name|bytes|
|-|-|
|command|1|
|analog 0|2|
|analog 1|2|
|analog 2|2|
|analog 3|2|
|analog 4|2|
|analog 5|2|
|PIND|1|
|PINB|1|
|PINC|1|

analog value is from 0 to 1023. PIND,PINB,PINC are a bitmap of Arduino pins(true/false per bit)
