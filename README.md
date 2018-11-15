# Homomorphic-Encryption

Homomorphic encryption is a form of encryption that allows computations to be carried out on **cipher text**, thus generating an encrypted result which, when decrypted, matches the result of operations performed on the plaintext.

This is sometimes a desirable feature in modern communication system architectures. Homomorphic encryption would allow the chaining together of different services without exposing the data to each of those services. For example, a chain of different services from different companies could calculate
- tax 
- the currency exchange rate 
- shipping, on a transaction without exposing the unencrypted data to each of those services.

Homomorphic encryption schemes are malleable by design. This enables their use in cloud computing environment for ensuring the confidentiality of processed data. In addition, the Homomorphic property of various cryptosystems can be used to create many other secure systems, for example secure voting systems,collision-resistant hash functions, private information retrieval schemes, and many more.

## Applications
- **Encrypted database querying :** 
  Typical database encryption leaves the database encrypted at rest, but when queries are performed the data must be decrypted in order to be parsed. Homomorphic encryption schemes have been devised such that database queries can run against ciphertext data directly.It must be noted that in this paper, the authors have accepted that they have used simple and non secure homomorphic scheme and still it takes a huge toll on the performance. For e.g. a 16 bit multiplication takes approximately 24 minutes.
  
- **Bitcoin split-key vanity mining :**
  Bitcoin addresses are hashes of public keys from ECDSA key pairs. A vanity address is an address generated from parameters such that the resultant hash contains a human-readable string (e.g., 1BoatSLRHtKNngkdXEeobR76b53LETtpyT). Given that ECDSA key pairs have homomorphic properties for addition and multiplication, one can outsource the generation of a vanity address without having the generator know the full private key for this address.

# ER Diagram

![ER Diagram](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/ER%20diagram.png)


# CRYPTOSYSTEM used - PAILLIER 

## Homomorphic Properties: 
- A notable feature of the Paillier cryptosystem is its homomorphic properties along with its non-deterministic encryption (see Electronic voting in Applications for usage). 
- Paillier cryptosystem has the property of additive homomorphism.
- If m1 and m2 are the message to be encrypted, E() and D() are the encryption and decryption function respectively and n is from the public Key, then the additive property can be expressed as follows. 
  - Homomorphic addition m1 + m2
    - The product of two cipher texts will decrypt to the sum of their corresponding plaintexts. 
      ```
      D(E(m1, public Key) ∗ E(m2, public Key) mod n2) )= m1 + m2 mod n
  - Homomorphic substraction m1 - m2
    - The product of two cipher texts will decrypt to the sum of their corresponding plaintexts only if m2 is converted into -m2
      ```
      negativem2=(E(m2,public key)^(new BigInteger("-1")) mod n2
      D(E(m1, public Key) ∗ negativem2 mod n2) )= m1 - m2 mod n
# Methods used:
### Key Generation: 
1. Choose two large prime numbers p and q randomly such that gcd(pq, (p −1)(q − 1))=1
2. Compute n = pq and Λ = lcm(p − 1, q − 1) 
3. Select random integer g where g ∈ Z*n2 
4. μ = (L(gΛ mod n2))−1 mod n where L(u) = (u-1)/n 
5. Public Key = (n, g) 
6. Private Key = (Λ, μ) 


### Encryption: 
1. Let m be a message to be encrypted and m ∈ Zn. 
2. Select random r where r ∈ Zn. 
3. Compute the cipher text c as c = gm.rn mod n2. 

### Decryption: 
1. Compute the message as m = L(cΛ mod n2).μ mod n. 

# Screenshots

### Login: 
![Login](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/login.jpg)

### Main menu: 
![Main menu](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/menu.jpg)

### Creating new account: 
![Creating new account](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/new%20account.jpg)

### Fund deposit: 
![deposit](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/deposit.jpg)

### Fund transfer: 
![Fund transfer](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/transfer.jpg)

### Checking balance and transactions: 
![Balance](https://github.com/aashutosh-ntyl/homomorphic-encryption/blob/master/balance.jpg)

# Future Scope
Homo-morphic encryption will be an useful technology in future as it allows an user to work on cipher text instead of decrypted text which is far more secure than other encryption.
