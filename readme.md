> ####注意事项
>> * 在idea中启动如果起动失败，可先执行maven命令：clean compile -Pdev

> ####打包说明
>> * 打包命令(开发)：clean package -Dmaven.test.skip=true -Pdev
>> * 打包命令(生产)：clean package -Dmaven.test.skip=true -Pprd