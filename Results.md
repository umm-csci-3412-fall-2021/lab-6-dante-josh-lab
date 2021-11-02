# Experimental results

_Briefly (you don't need to write a lot) document the results of your
experiments with throwing a bunch of clients at your server, as described
in the lab write-up. You should probably delete or incorporate this text
into your write-up._

_You should indicate here what variations you tried (every connection gets
a new thread, using a threadpool of size X, etc., etc.), and what the
results were like when you spun up a bunch of threads that send
decent-sized files to the server._


With the single threaded implementation of the EchoServer, running the test script with 500 clients and the words.txt file, we observed runtimes in the range of 20-24 seconds. This is compared to the multithreaded implementations of the EchoServer, such as the fixed thread pool, cached thread pool, and creating new threads for every client connection, which had runtimes of around 16-18 seconds. Additionally, all implementations crashed around client counts of 600. It is worth noting that using the fixed thread pool, we were only using a thread count of 3. Increasing the thread pool size did affect run time of the fixed thread pool implementation of our tests and also crashed at around 600 clients.
