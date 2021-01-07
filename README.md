# Simple-IoT-Hacking-Script
This is for demonstration purpose only and theory on the automation process to hack an IoTs SSH service.

This is a theory script on how to automate the process of hacking into IoT devices and devices in general.
This script spcifically finds open SSH services running on IoTs that were stated in the user's search input.
The process has 3 stages:
* Scanning and finding IPs with shodan. 
* Brute forcing the SSH creds with Hydra.
* Finally, run an automated attack.

## Usage:
Usage: ./<script_name> <usernames_file> <passwords_file> (This will be provided for hydra later)
    echo "    Example: ./IoTScanner usernames.lst passwords.lst"
