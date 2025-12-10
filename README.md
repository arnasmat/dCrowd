# dCrowd android app

Android app for a decentralized ethereum smart contract-based crowdfunding application.

# How to set up

1. Deploy backend `ganache-cli` and `truffle migrate` (for dev)
2. Download apk from releases or build application yourself how you typically would for android
3. Launch on emulator or mobile phone
4. Go to settings, set up correct urls (note: if on ganache emulator to localhost is typically `http://10.0.2.2:8545`) and contract addresses (outputted after truffle migrate)
5. Go back, refresh feed and enjoy :)


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
