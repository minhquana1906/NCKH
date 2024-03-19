import cv2
import socket
import struct
import pickle

# Khởi tạo socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(('localhost', 8485))
server_socket.listen(1)

# Chờ kết nối từ client
conn, addr = server_socket.accept()

# Mở webcam
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    # Serialize frame
    data = pickle.dumps(frame)

    # Gửi dữ liệu qua socket
    message_size = struct.pack("L", len(data))
    conn.sendall(message_size + data)