echo Deploying version $VERSION
wget -q -O /dev/null http://localhost:33221/shutdown
rm continuous-delivery-training-*.jar
wget http://10.0.2.2:18081/artifactory/libs-release/sourcetalk-tage/continuous-delivery-training/$VERSION/continuous-delivery-training-$VERSION.jar
nohup java -jar continuous-delivery-training-$VERSION.jar 0<&- &>/dev/null &
