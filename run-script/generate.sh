#!/bin/bash

CURRENT_DIR=$(pwd)
RELATIVE_DIR=$(dirname "$0")
CONFIG_FILE="$CURRENT_DIR/$RELATIVE_DIR/config.properties"

function getProperty {
   PROP_KEY=$1
   PROP_VALUE=`cat $CONFIG_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
   echo $PROP_VALUE
}

CONTEXT_PATH=$(getProperty "CONTEXT_PATH")
echo "CONTEXT_PATH=$CONTEXT_PATH"
sed -i "s|context-path: /starter|context-path: $CONTEXT_PATH|g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"
sed -i "s|/starter|$CONTEXT_PATH|g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/test/groovy/pl/aswit/starter/e2e/config/RestApiTestBase.groovy"

APPLICATION_DESC=$(getProperty "APPLICATION_DESC")
echo "APPLICATION_DESC=$APPLICATION_DESC"
sed -i "s/name: Portal Starter/name: $APPLICATION_DESC/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

APPLICATION_NAME=$(getProperty "APPLICATION_NAME")
echo "APPLICATION_NAME=$APPLICATION_NAME"
sed -i "s/name: STARTER/name: $APPLICATION_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

echo '${AnsiColor.BRIGHT_GREEN}' > portal-be-starter-main/application/src/main/resources/banner.txt
curl "https://devops.datenkollektiv.de/renderBannerTxt?text=${APPLICATION_NAME}&font=standard" >> portal-be-starter-main/application/src/main/resources/banner.txt
echo '${spring.application.name} v.${app.version}' >> portal-be-starter-main/application/src/main/resources/banner.txt
echo '${AnsiColor.BLUE}.: Running Spring Boot ${spring-boot.version} :. ${AnsiColor.DEFAULT}' >> portal-be-starter-main/application/src/main/resources/banner.txt

GIT_REPO_NAME=$(getProperty "GIT_REPO_NAME")
echo "GIT_REPO_NAME=$GIT_REPO_NAME"
sed -i "s/portal-be-starter/$GIT_REPO_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../settings.gradle"
sed -i "s/portal-be-starter/$GIT_REPO_NAME-local/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/local.properties"
find . -type f -name 'build.gradle' | xargs sed -i "s/portal-be-starter/$GIT_REPO_NAME/g"

PACKAGE_NAME=$(getProperty "PACKAGE_NAME")
echo "PACKAGE_NAME=$PACKAGE_NAME"
find . -type f \( -name '*.java' -o -name '*.groovy' \) | xargs sed -i "s/pl.aswit.starter/pl.aswit.$PACKAGE_NAME/g"
sed -i "s/pl.aswit.starter/pl.aswit.$PACKAGE_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../build.gradle"
sed -i "s/pl.aswit.starter/pl.aswit.$PACKAGE_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/build.gradle"
sed -i "s/pl.aswit.starter/pl.aswit.$PACKAGE_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

for f in `find portal-be-starter-main -type d -name starter`; do
    mv -- "$f" "${f%/starter}/$PACKAGE_NAME"
done
mv portal-be-starter-main "$GIT_REPO_NAME-main"

echo "TODO" > "$CURRENT_DIR/$RELATIVE_DIR/../README.md"


echo "Self destroying..."
rm -rf "$CURRENT_DIR/$RELATIVE_DIR/../run-script"

echo "Project generated"
