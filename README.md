# cups-to-mqtt

[![mqtt-smarthome](https://img.shields.io/badge/mqtt-smarthome-blue.svg)](https://github.com/mqtt-smarthome/mqtt-smarthome)

Convert the CUPS printer queues to mqtt messages

## Example message

The message will be posted to the following topic:
Example: `cups/brother_hl_2070n`

```json
{
  "jobs-completed":499,
  "jobs-queued;":0
}
```

## Example configuration

```json
{
  "mqtt": {
    "url": "tcp://192.168.2.98:1883",
    "username": "user",
    "password": "password",
    "deduplicate": true
  },
  "cups": {
    "host": "192.168.3.15",
    "port": 631,
    "username": "anonymous"
  }
}
```

# Deduplicate messages

When `deduplicate` is set to `true` no duplicate mqtt messages will be sent.

# Docker

This application is intended to be executed using docker. Example docker compose usage:

```
cupsmqtt:
  hostname: cupsmqtt
  image: pharndt/cupsmqtt:1.0.1
  volumes:
    - ./config/cupsmqtt:/var/lib/cupsmqtt:ro
  restart: always 
  depends_on:
   - mosquitto
```
