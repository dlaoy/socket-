from socket import *

# IP地址为空表示接收任何网段的广播消息
address = ('', 10130)

# 创建流式socket
s = socket(AF_INET, SOCK_DGRAM)

# 设置socket属性
s.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)

# 绑定本地ip地址和端口
s.bind(address)

print('Waiting to receive...')

try:
    while True:
        # 接收消息
        data, address = s.recvfrom(1024)
        
        print('[Received from %s:%d]: %s' % (address[0], address[1], data.decode()))

        # 单播返回消息给发送者
        response = 'This is the response message!'
        s.sendto(response.encode(), (address[0], 10131))
except KeyboardInterrupt:
    print("Ctrl+C detected, exiting program")

# 关闭socket
s.close()
