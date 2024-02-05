# Secret-Chat
Multithreaded Server Socket Chat App with Time Sensitive Messages

This project is a multithreaded server socket chat application that supports multiple users and allows real-time communication. Messages automatically disappear after a predetermined timeout period, providing immediate attention and fast repsonses before the private chat disappears.

#Key Features:
- Multithreaded Server + Mulit-user Support: Supports seamless interaction between numerious clients
- User-friendly GUI: Provides a nostalic (retro 90's vibe) and intuitive UI with fast modern technology
- Ephemeral messages: Messages self-destruct after a predefined timeframe.

#Installation: 
1. CLone the repo: 
git clone https://github.com/sun-tina/Secret-Chat.git
2. Open project on preferred IDE (Visual Studio Code Reccomended)
3. Build and Run:
     - run server.java FIRST ( to get the server running)
     - run client.java (run as many times for as many users/chat windows you want)
  *Both server and client are in src*
4. Add username when prompted
5. Start chatting!
6. Have fun and customize your own chat box by changing the background color theme and text color

#To close 
Do not close SERVER window BEFORE closing client. 
Server must be running inorder to support chatters. Close CLIENT chat window of the user you no longer need, then CLOSE SERVER LAST when there are no more client windows open.
