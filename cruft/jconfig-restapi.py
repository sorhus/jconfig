from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import sys
import MySQLdb

class ConfigHandler(BaseHTTPRequestHandler):

    def get_db():
        return MySQLdb.connect(
            host=config['HOST'],
            db=config['DB'],
            user=config['USER'], 
            passwd=config['PASSWORD']
        )

    def do_GET(self):
        db = get_db()
        c = db.cursor()
        rows = c.execute("SELECT json FROM configs WHERE id=%s", (self.path[1:]))
        if rows == 1:
            self.send_response(200)
            self.send_header('Content-type','application/json')
            self.end_headers()
            self.wfile.write(c.fetchone()[0])
        else:
            self.send_response(404)
        c.close()
        db.close()

try:

    if len(sys.argv) != 2:
        raise Exception("No config path specified.")

    f = open(sys.argv[1],'r')
    parse = lambda x: x.replace(" ","").strip().split("=")
    config = dict(map(parse, f))
    f.close()

    port = int(config['PORT'])
    server = HTTPServer(('', port), ConfigHandler)
    print 'Started http server on port', port
    server.serve_forever()

except KeyboardInterrupt:
    print 'Shutting down http server'
    server.socket.close()

