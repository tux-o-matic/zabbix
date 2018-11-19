Nginx status
=====

nginx
-----

	server {
		listen       80;
		server_name  localhost;

		location /server-status {
			stub_status on;
			access_log off;
			allow 127.0.0.1;
			deny all;
		}
	}

zabbix_agentd
-----
In Hiera YAML:
```yaml
zabbix_userparameters:
  nginx:
    content: UserParameter=nginx.status[*],/etc/zabbix/scripts/nginx.sh $1 %{::fqdn}
```

### Test scripts

	chmod +x /srv/zabbix/libexec/nginx.sh

	# /srv/zabbix/libexec/nginx.sh
	Usage /srv/zabbix/libexec/nginx.sh {active|accepts|handled|requests|reading|writing|waiting}
	# /srv/zabbix/libexec/nginx.sh accepts
	82

	# systemctl restart zabbix-agent.service

### Test Agent

	# yum install -y zabbix-get

	# zabbix_get -s <agent_ip_address> -k 'nginx.status[accepts]'
	109

Import Template
-----
	Import file: choice xml file
	click "import" button

	Imported successfully
