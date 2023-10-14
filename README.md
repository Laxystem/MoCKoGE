# MoCKoGE
<p align="center">
  <image src=".idea/icon.png" alt="MoCKoGE Icon" width=256/>
  <br/>
  <i>The <b>Mo</b>dular <b>C</b>oncurrent <b>Ko</b>tlin <b>G</b>ame <b>E</b>ngine</i>
</p>

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
