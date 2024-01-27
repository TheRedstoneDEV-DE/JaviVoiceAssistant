# Pipewire fix for Java Audio
1. install pipewire-jack if you haven't already:
  - Arch Linux:    `sudo pacman -S pipewire-pulse`
  - Ubuntu,Debian: `sudo apt install pipewire-audio-client-libraries`
2. look for pipewire in loaded alsa config:
  - `alsactl dump-cfg | grep pipewire`
  - if it contains it continue
3. dump loaded alsa config to a know location:
  - `alsactl dump-cfg > /path/to/somefile.conf`
4. load dumped alsa config at programm startup:
  - `ALSA_CONFIG_PATH=/path/to/somefile.conf [COMMAND]`
