/*
* Author: rootomatic
* A device handler to integrate Orvibo s20 switches with a raspberry pi backend
*/
preferences {
    section("Raspberry Pi Config"){
        input "internal_ip", "text", title: "Raspberry Pi IP", required: false
        input "internal_port", "text", title: "Internal Port", required: false
    }
    section("Socket Details"){
        input "socket_mac", "text", title: "Socket Mac", required: false
        input "socket_ip", "text", title: "Socket IP", required: false
    }
}

metadata {
    definition (name: "Orvibo S20 Switch", namespace: "rootomatic", author: "rootomatic", description: "A device handler to integrate Orvibo s20 switches with a raspberry pi backend") {
        capability "Switch"
    }
}

simulator {
}

tiles(scale: 2) {
    standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
        state "off", label: "off", action: "switch.on",
        icon: "st.switches.switch.off", backgroundColor: "#ffffff"
        state "on", label: "on", action: "switch.off",
        icon: "st.switches.switch.on", backgroundColor: "#79b821"
    }
    main("switch")
    details(["switch"])
}

def on() {
    log.debug "turning On"
    httpCall("on")
}

def off() {
    log.debug "turning Off"
    httpCall("off")
}

def httpCall(path){
    def result = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: "/socket/${socket_ip}/${socket_mac}/${path}",
    	headers: [
    	HOST: "${internal_ip}:${internal_port}"
    	]
    )
    sendHubCommand(result)
    log.debug "Executing ON"
    sendEvent(name: "switch", value: "on")
    log.debug result
}