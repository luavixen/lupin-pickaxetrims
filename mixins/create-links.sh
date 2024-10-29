#!/bin/sh

export MSYS=winsymlinks:nativestrict

mod_config_name_shared="pickaxetrims.mixins-shared.json"
mod_config_name_client="pickaxetrims.mixins-client.json"
mod_source_path="dev/foxgirl/pickaxetrims"

cd ..

for loader in fabric forge; do

    if [ -d $loader ]; then
        (
            echo "Creating links for loader $loader..."
            set -o xtrace

            cd $loader/src/main || exit

            (
                cd resources || exit
                rm $mod_config_name_shared $mod_config_name_client
                ln -s ../../../../mixins/$mod_config_name_shared $mod_config_name_shared
                ln -s ../../../../mixins/$mod_config_name_client $mod_config_name_client
            )

            (
                cd java/$mod_source_path || exit
                (
                    cd shared || exit
                    rm mixin
                    ln -s ../../../../../../../../mixins/shared/mixin mixin
                )
                (
                    cd client || exit
                    rm mixin
                    ln -s ../../../../../../../../mixins/client/mixin mixin
                )
            )
        )
    fi

done
