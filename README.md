# vodafone-test
Test project for Vodafone job application

To start the mysql server:  
```bash
docker-compose up -d
```

To create the database in the mysql server:  
Connect to the db server with:  
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
and enter the password "*password*".  
Run these commands on the mysql server:
```bash
mysql> create database db_example; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'password'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```

Start the server with:
```bash
./mvnw spring-boot:run
```

Use the `/networks` path to interact with the REST API. On HTTP POST the server expects a JSON containing the network configuration like this:  
```JSON
{
  "Berlin": {
    "BER-1": {
      "security_level": 1,
      "networks": {
        "192.168.0.0/24": [
          {
            "address": "255.255.255.0",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168..0.3",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.288",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "invalid",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.1",
            "available": false,
            "last_used": "30/01/20 16:00:00"
          },
          {
            "address": "192.168.0.4",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.2",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.3",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.1.1",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          }
        ],
        "10.0.8.0/22": [
          {
            "address": "10.0.11.254",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "10.0.8.1",
            "available": false,
            "last_used": "30/01/20 16:00:00"
          },
          {
            "address": "10.0.8.0",
            "available": false,
            "last_used": "30/01/20 16:00:00"
          },
          {
            "address": "10.0.12.1",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "10.0.10.a",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          }
        ]
      }
    },
    "BER-203": {
      "security_level": 3,
      "networks": {
        "192.168.10.0/24": [
          {
            "address": "192.168.10.8",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.10.5",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.10.6",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.7",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          }
        ],
        "192.168.11.0/24": [
          {
            "address": "192.168.11.1",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.2.1",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.11.522",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          }
        ]
      }
    },
    "BER-4000": {
      "security_level": 3,
      "networks": {
        "192.168.100.0/24": [
          {
            "address": "192.168.100.1",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          }
        ]
      }
    },
   "TEST-1": {
    "security_level": 3,
    "networks": {
      "192.168.200.0/24": [
        {
          "address": "192.168.200.8",
          "available": true,
          "last_used": "30/01/20 17:00:00"
        }
      ]
    }
  }
  },
  "Paris": {
    "PAR-1": {
      "security_level": 5,
      "networks": {
        "192.168.203.0/24": [
          {
            "address": "192.168.203.20",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.203.21",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.203.19",
            "available": false,
            "last_used": "30/01/20 17:00:00"
          },
          {
            "address": "192.168.0.0",
            "available": true,
            "last_used": "30/01/20 17:00:00"
          }
        ]
      }
    },
    "XPAR-2": {
      "security_level": 0,
      "networks": {

      }
    }
  }
}
```
On HTTP GET the server expects a querystring parameter named `ip` defining the ip address to be checked. Example:
```
127.0.0.1:8080/networks?ip=192.168.0.1
```

Use any HTTP client like Postman or curl to test the app.
