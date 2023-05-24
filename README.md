# dchat_server
A server to transfer message for decentralized chat client using ktor. It consists of a login api, a message send api and a websocket.

## /login
The client will send the signature of a totp code with its private key to this api, and this api will validate the signature by the public key.
If signature is valid, return a token to the client. Then the client will connect to the ws to receive message using this token.  

## /message/send
This api receive the message sent by the client.It will validate the signature of message to ensure the message is from the real user like login api do.
then, it will send this message to the user by websocket if the user is online.