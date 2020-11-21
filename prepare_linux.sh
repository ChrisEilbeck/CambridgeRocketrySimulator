# prepare /usr/share/camrocsim and give access to all users
# sudo mkdir -p -m=777 /usr/share/camrocsim/
# sudo chmod 777 /usr/share/camrocsim

# make sure binary is prepared!
# to compile the binary, goto cpp/ and type "make"

# copy relevant folders Data & simulator binary to the usr/share folder
#cp -r Data/ /usr/share/camrocsim/
#cp -r simulator/ /usr/share/camrocsim/
#cp -r Plotter/ /usr/share/camrocsim/

cp -r Data ~/.camrocsim/
cp -r simulator ~/.camrocsim/
cp -r Plotter ~/.camrocsim/

echo "succces"
