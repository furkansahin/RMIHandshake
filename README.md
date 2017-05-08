# RMIHandshake
This project is created by Mehmet Furkan Sahin for a homework assignment in the course CS442 - Distributed Systems.
Note: This RMI structure is created and tested in local server.

## How to use?
1. Firstly, you need to run RMIServer.java (You can run it directly without providing any arguments)
2. You should start a client from the same computer. (If needed to run remotely, RMIClient.java registry should be fetched from the server where RMIServer.java runs)
When you run the RMIClient, you need to give 2 arguments respectively [UniqueName] [TimeoutTime]
So an example run would be;

```
java RMIClient furkan 10
```

3. If you connect another client before missing the deadline as described in the step 2, they are going to be matched and send messages
  - Selaminaleykum
  - Aleykumselam

![alt text](https://cloud.githubusercontent.com/assets/6233557/25806651/9889c8aa-340c-11e7-99eb-541f18801f50.png)
