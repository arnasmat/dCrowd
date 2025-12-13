# dCrowd android app

Android app for a decentralized ethereum smart contract-based crowdfunding application.

# How to set up

1. Deploy backend `ganache-cli` and `truffle migrate` (for dev)
2. Download apk from releases or build application yourself how you typically would for android
3. Launch on emulator or mobile phone
4. Go to settings, set up correct urls (note: if on ganache emulator to localhost is typically `http://10.0.2.2:8545`) and contract addresses (outputted after truffle migrate)
5. Go back, refresh feed and enjoy :)

# Usage screenshots

(Note: at the start you will have to connect to the network by providing an RPC url and a contract address, as well as log in with an account. You can do so in the settings.)

<img width="250" alt="Main screen" src="https://github.com/user-attachments/assets/8c51dc03-f859-435a-aeef-2b6ed9bae27a" />
<img width="250" alt="Settings" src="https://github.com/user-attachments/assets/636305a7-944a-4509-a1d1-58e0974a764b" />
<img width="250" alt="Project creation screen" src="https://github.com/user-attachments/assets/1c066e6b-68ba-4383-bd6d-63a990700ed2" />
<img width="250" alt="Example of an error" src="https://github.com/user-attachments/assets/211b99da-1d4c-4dad-a1f0-6f3701586560" />
<img width="250" alt="Project list screen" src="https://github.com/user-attachments/assets/5d747d18-0e01-49a7-8de2-f759b72fe999" />
<img width="250" alt="Project details screen" src="https://github.com/user-attachments/assets/676a89b8-b8fb-42bb-9ec9-765a22c64000" />
<img width="250" alt="Halfway funded project" src="https://github.com/user-attachments/assets/2c4e0285-fb8a-41a4-8391-7412fbeb0545" />
<img width="250" alt="Fully funded, cancelled or failed projects become inactive" src="https://github.com/user-attachments/assets/fc71ed1e-a811-4e62-913f-e32e032fdeea" />
<img width="250" alt="Multiple projects in the list screen" src="https://github.com/user-attachments/assets/4cdc41ac-2c07-4628-a59d-99c21922c4f6" />





## Tech stack

web3j for intraction with smart contracts and web3 stuff & crowdsourcing.java autogen

kotlin because ofc

jetpack compose & material3 for UI

navigation3 for navigation (wanted to test it out as it's new)

hilt for DI

coil for images

datastore for local storage

---



In development to update autogen crowdsourcing file (note: path may vary for you)

```bash
web3j generate truffle --truffle-json build/contracts/CrowdSourcing.json -o ~/AndroidStudioProjects/dCrowd/app/src/main/java/ -p com.arnasmat.dcrowd.data.sol
```
