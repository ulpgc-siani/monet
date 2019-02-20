echo -n "ID: "  > version.txt; git log | head -n 4 | grep commit | awk 'BEGIN{FS="[ :]+"}{print $2}' >> version.txt;
echo -n "Author: "  >> version.txt; git log | head -n 4 | grep Author | awk 'BEGIN{FS="[ :]+"}{print $2 " " $3 " " $4 " " $5 " " $6}' >> version.txt;
echo -n "Branch: " >> version.txt
git branch | grep "*" | awk 'BEGIN{FS="[ :]+"}{print $2}' >> version.txt
echo -n "Generate WAR date: " >> version.txt
date +"%d/%m/%Y %H:%M" >> version.txt
