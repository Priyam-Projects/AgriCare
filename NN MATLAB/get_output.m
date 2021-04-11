function out_val = get_output( Theta1 , Theta2 , X )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here

m = 1 ;

X =  [1 X] ;

a2 = sigmoid( Theta1 * (X') ) ;

a2 = [ ones( 1, m ) ; a2 ] ;

a3 = Theta2 * ( a2 ) ;

a3 = sigmoid(a3) ;

[~,idx] = max(a3,[],1) ;

out_val = idx ;

end

