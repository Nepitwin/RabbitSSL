# Copyright 2017-2018 Andreas Sekulski
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from configuration import *

try:
    with pika.BlockingConnection(parameters) as connection:
        channel = connection.channel()
        channel.queue_declare(queue=rabbit_queue_opts["queue"])

        channel.basic_publish(exchange='',
                              routing_key=rabbit_queue_opts["queue"],
                              body=rabbit_queue_opts["message"])

        print(" [x] Sent '" + rabbit_queue_opts['message'] + "!'")
except BaseException as e:
    print(str(e), e.__class__.__name__)
