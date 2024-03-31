from flask import Flask, request, jsonify
from openai import OpenAI
import requests
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello, World!'

@app.route('/fetch_data', methods=['POST'])
def fetch_data_Backend():
    
    data = request.json
    # Check
    #print("Received data from Backend:", data)
    user_id = data.pop('user_id', None)
    care_taker_email = data.pop('care_taker', "")
    user_name = data.pop('name', "")
    #print(f"User is: {user_id} and data is {data}")
    chatgpt_response = give_response(data, care_taker_email, user_name)
    push_response_Backend(chatgpt_response)
    return jsonify({'response': chatgpt_response})
@app.route('/chat', methods=['GET'])
def give_response(data, email_id, name):

    response = push_data_ChatGPT(data)
    print(response)
    '''if "response == "Concerning"":
        return "Inform CareTaker(s) immediately"
    else:
        return "So far so good!"'''
    if "not" in response:
        message = "So far so good!"
    else:
        message = "Kindly check up on"
    send_mail_caretaker(message, name, email_id)
    return response

def push_data_ChatGPT(data):

    # Initialize OpenAI client with your API key
    client = OpenAI(api_key="API_KEY")
    strData = ""
    for key in data:
        strData += str(key) + ":" + str(data[key]) + ","
    prompt = "Based on the give data. Do you think this person's mental health is at a concerning stage and their caretakers should be informed to check on them? Answer in concerning/non concerning where the value is the time in hours/minutes" + strData
    #prompt = "Based on the give data. Do you think this person's mental health is at a concerning stage and their caretakers should be informed to check on them? where the value is the time in hours/minutes" + strData
    # Send prompt to OpenAI API for completion
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{
            "role": "user",
            "content": prompt
        }]
    )

    # Extract and return response from OpenAI
    generated_text = response.choices[0].message.content
    #return jsonify({'response': generated_text})
    return generated_text

@app.route('/sender_route', methods=['GET'])
def push_response_Backend(response):
    # Generate the response data
    # Send the response data to the receiver route
    send_to_receiver(response)

    return jsonify(response)

def send_to_receiver(data):
    # URL of the receiver route
    receiver_url = "http://127.0.0.1:5000/receiver_route"

    # Send a POST request to the receiver route
    response = requests.post(receiver_url, json=data)

    # Check if the request was successful
    if response.status_code == 200:
        print("Response sent to receiver route successfully")
    else:
        print("Failed to send response to receiver route")
@app.route('/contact', methods=['POST'])
def send_mail_caretaker(message, name, email_id):
    if email_id:
        smtp_server = "smtp.gmail.com"
        smtp_port = 587
        smtp_username = "insight.mentalhealthchecker@gmail.com"
        smtp_password = "debb djsf wugf dydj"
        to_email = email_id
        subject = "Mental Health Alert"
        message_body = f"Mental Health of the user {name} is {message}"
        
        msg = MIMEMultipart()
        msg['From'] = smtp_username
        msg['To'] = to_email
        msg['Subject'] = subject
        msg.attach(MIMEText(message_body, 'plain'))
        
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()
            server.login(smtp_username, smtp_password)
            server.send_message(msg)
    return "Sent Email"
    

if __name__ == '__main__':
    app.run(debug=True)
