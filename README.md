# MoCKoGE

> *The **Mo**dular **C**oncurrent **Ko**tlin **G**ame **E**ngine*

## Project Structure

MoCKoGE is separated to subprojects:

- [Core](core/README.md)

Each subproject is separated to three subprojects of its own:

- [Common](#common)
- [Client](#client)
- [Server](#server)

### Common

Handles logic common to the client and the server.

### Client

Dependent on the server, it includes client-side logic such as rendering.

The actual client includes a server that it is able to communicate to, and it is additionally capable of talking to
another, remote server.

### Server

Contains server-side logic.
