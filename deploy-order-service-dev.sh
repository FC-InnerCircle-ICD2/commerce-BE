#!/bin/sh
APP="order-service"
PROFILE="dev"

DEPLOY_APP="${APP}-${PROFILE}"
DEPLOY_VERSION="`date +%Y%m%d-%H%M%S`"
DEPLOY_FILES="order-service-1.0.jar Procfile .ebextensions .platform startup.sh"
DEPLOY_VERSION_LABLE="${DEPLOY_APP}-${DEPLOY_VERSION}"
DEPLOY_FILENAME="${DEPLOY_APP}-${DEPLOY_VERSION}.zip"

echo "now deploy ${DEPLOY_APP}-${DEPLOY_VERSION}"

# 1. delete old deploy resource files.
/bin/rm -rf eb-deploy/${DEPLOY_APP}
mkdir -p eb-deploy/${DEPLOY_APP}

# 2. build
./gradlew clean :order:build -x test

# 3. copy resources
cp -f order/build/libs/order-0.0.1.jar eb-deploy/${DEPLOY_APP}/
cp -f eb-deploy-template/${DEPLOY_APP}/* eb-deploy/${DEPLOY_APP}/
cp -rf eb-deploy-template/${DEPLOY_APP}/.ebextensions eb-deploy/${DEPLOY_APP}/
cp -rf eb-deploy-template/${DEPLOY_APP}/.platform eb-deploy/${DEPLOY_APP}/

# 4. update permission
chmod ugo+x eb-deploy/${DEPLOY_APP}/Procfile
chmod ugo+x eb-deploy/${DEPLOY_APP}/startup.sh

cd eb-deploy/${DEPLOY_APP}/ && \
zip -r ${DEPLOY_FILENAME} ${DEPLOY_FILES} && \
cd -

sleep 3

aws s3 cp eb-deploy/${DEPLOY_APP}/${DEPLOY_FILENAME} s3://innercircle-ecommerce-releases/order-service/${PROFILE}/${APP}/${DEPLOY_FILENAME} && \
aws elasticbeanstalk create-application-version --application-name ${APP} --version-label ${DEPLOY_VERSION_LABLE} --source-bundle S3Bucket=innercircle-ecommerce-releases,S3Key=order-service/${PROFILE}/${APP}/${DEPLOY_FILENAME} --no-paginate --no-cli-pager && \
aws elasticbeanstalk update-environment --application-name ${APP} --environment-name ${DEPLOY_APP} --version-label ${DEPLOY_VERSION_LABLE} --no-paginate --no-cli-pager && \
echo "done."