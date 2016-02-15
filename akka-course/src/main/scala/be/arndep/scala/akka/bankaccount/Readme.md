#Bank account solution in actor model

Each account is represented as an actor.
On an account we can deposit and withdraw money (if it has sufficient founds).

An account only knows itself, it cannot know another actor.

That's why we have developed another actor 'WireTransfer'.
This actor is responsible to transfer money from one account to another and it will be success only if it can withdraw the first account.

An example of that has been implemented in the TransferMainTest actor (in test section).
You can try to modify the transfer amount by 150 and see the difference in log.

##Remarks

add VMOptions for debug logs: -Dakka.loglevel=DEBUG -Dakka.actor.debug.receive=on

##TODOs

A message between 2 actors can be lost.
So to solve this, we can implement a redelivery policy in the sender: 

* The sender, redelivers the message until it receives an ACK from the receiver. In this situation we have to implement an idempotent repository in the receiver.
 
So we need to add: 
* log activities of WireTransfer in persistent storage
* each transfer has a unique id
* the same id in the Deposit and Withdraw message
* store IDs of completed actions in BankAccount