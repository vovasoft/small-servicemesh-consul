package main

import (
	"fmt"
	"github.com/hashicorp/consul/api"
	"log"
	"net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintln(w, "Hello from Golang Service!")
}

func registerServiceWithConsul() {
	config := api.DefaultConfig()
	consulClient, err := api.NewClient(config)
	if err != nil {
		log.Fatalf("Failed to connect to Consul: %v", err)
	}

	registration := &api.AgentServiceRegistration{
		ID:      "golang-service",
		Name:    "golang-service",
		Address: "localhost",
		Port:    9090,
	}

	err = consulClient.Agent().ServiceRegister(registration)
	if err != nil {
		log.Fatalf("Failed to register service with Consul: %v", err)
	}

	log.Println("Service registered with Consul")
}

func main() {
	// 注册服务到 Consul
	registerServiceWithConsul()

	// 设置路由
	http.HandleFunc("/hello", helloHandler)

	// 启动服务器
	log.Println("Starting Golang service on port 9090...")
	err := http.ListenAndServe(":9090", nil)
	if err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
