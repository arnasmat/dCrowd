# dCrowd android app

Android app for a decentralized ethereum smart contract-based crowdfunding application.

# Application screenshots

<img width="250" alt="Main screen" src="https://github.com/user-attachments/assets/8c51dc03-f859-435a-aeef-2b6ed9bae27a" />
<img width="250" alt="Settings" src="https://github.com/user-attachments/assets/636305a7-944a-4509-a1d1-58e0974a764b" />
<img width="250" alt="Project creation screen" src="https://github.com/user-attachments/assets/1c066e6b-68ba-4383-bd6d-63a990700ed2" />
<img width="250" alt="Example of an error" src="https://github.com/user-attachments/assets/211b99da-1d4c-4dad-a1f0-6f3701586560" />
<img width="250" alt="Project list screen" src="https://github.com/user-attachments/assets/5d747d18-0e01-49a7-8de2-f759b72fe999" />
<img width="250" alt="Project details screen" src="https://github.com/user-attachments/assets/676a89b8-b8fb-42bb-9ec9-765a22c64000" />
<img width="250" alt="Halfway funded project" src="https://github.com/user-attachments/assets/2c4e0285-fb8a-41a4-8391-7412fbeb0545" />
<img width="250" alt="Fully funded, cancelled or failed projects become inactive" src="https://github.com/user-attachments/assets/fc71ed1e-a811-4e62-913f-e32e032fdeea" />
<img width="250" alt="Multiple projects in the list screen" src="https://github.com/user-attachments/assets/4cdc41ac-2c07-4628-a59d-99c21922c4f6" />

# How to set up (for testing)

### 1. Set up and deploy backend
Prerequisites: set up [ganache-cli and truffle](https://archive.trufflesuite.com/docs/truffle/how-to/install/)
1.1. [Clone the solidity backend.](https://github.com/arnasmat/Blockchain-4-dApps)
1.2 Run `ganache-cli` in one terminal and `truffle migrate` in another in the cloned directory.
1.3 Write down the contract address from `truffle migrate` and at least one of the accounts public and private keys from `ganche-cli`. They will be needed later.

### 2. Android
2.1. Download apk from releases or build the application yourself how you typically would for android (The recommended method is by cloning it and building it via android studio).
2.2. Launch the downloaded application on an Android emulator or mobile phone.
2.3. Go to the settings:
 - If running on emulator and `ganache-cli` is hosted on localhost, set RPC url as `http://10.0.2.2:8545`
 - Enter the previously copied contract address received from `truffle migrate`
 - Log in with one of the accounts from the output of `ganache-cli` with a public and private key.
 - Save
2.4. Go back to feed and refresh (swipe down)

### 3. (Optional) Further development
3.1. If you plan on developing this application further, you will also need to set up [Web3j](https://docs.web3j.io/latest/quickstart/) to automate `data.sol.CrowdSourcing.java` generation. This is how you would typically autogen that file with Web3j CLI: (note - this is assuming you are running it from the backend folder, you will have to change path to dCrowd's android app)

```bash
web3j generate truffle --truffle-json build/contracts/CrowdSourcing.json -o /pathTo/dCrowd/app/src/main/java/ -p com.arnasmat.dcrowd.data.sol
```

## Android tech stack

- [Web3j](https://docs.web3j.io/latest/quickstart/) for interaction with smart contracts, files to interact with solidity (CrowdSourcing.java) autogeneration
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/compose) & [Material3](https://m3.material.io/) for UI
- [Navigation3](https://developer.android.com/guide/navigation/navigation-3) for navigation (Wanted to test it out as it's newly released)
- [Dagger-Hilt](https://dagger.dev/hilt/) for Dependency Injection
- [Coil](https://coil-kt.github.io/coil/) for loading network images
- [Jetpack DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) for local storage 
