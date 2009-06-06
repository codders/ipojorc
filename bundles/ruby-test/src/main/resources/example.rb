require 'net/http.rb'

puts Net::HTTP.get_print(URI.parse("http://www.google.com"))
puts "Another hello world!"
