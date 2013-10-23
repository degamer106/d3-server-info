d3-server-info
==============

Diablo 3 Server Info App

This Android app simply extracts the relevant HTML data from the Diablo 3 Server status webpage (http://us.battle.net/d3/en/status) and re-processes the information so that it can be displayed in a more mobile-friendly format.

Features
========
See Google Play: https://play.google.com/store/apps/details?id=com.degamer106.serverinfo&hl=en

Usage
=====
Should be pretty intuitive.  Azmodan's eyes will shine a bright green if the Servers are ONLINE for a particular region or red for OFFLINE.

Tools Used
==========
GIMP - Color sampling
Eclipse IDE - Android/Java development environment
Firefox SQLite Manager plugin - Verifying the database was correct
Makeappicon - Online website that generates icons for different screen DPIs.

Improvements
============
- Getting rid of the SQLite DB.  In hindsight, I realized that there was no real point in having a database.  I just wanted to play around with the APIs a bit.
- Code refactoring.

Future plans
============
Well, since Blizzard officially announcement the removal of the Auction Houses on March 18, 2014, the app's UI will have to be re-worked to accomdate those changes.

Credits
=======
Diablofans.com - Got the idea for different color schemes and fonts from there.
DeviantArt - Great place to find art work (with permission) to use in your apps.
