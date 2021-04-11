%% Machine Learning Online Class - Exercise 4 Neural Network Learning

%  Instructions
%  ------------
% 
%  This file contains code that helps you get started on the
%  linear exercise. You will need to complete the following functions 
%  in this exericse:
%
%     sigmoidGradient.m
%     randInitializeWeights.m
%     nnCostFunction.m
%
%  For this exercise, you will not need to change any code in this file,
%  or any other files other than those mentioned above.
%

%% Initialization
clear ; close all; clc

%% Setup the parameters you will use for this exercise
       % 10 labels, from 1 to 10   
                          % (note that we have mapped "0" to label 10)

%% =========== Part 1: Loading and Visualizing Data =============
%  We start the exercise by first loading and visualizing the dataset. 
%  You will be working with a dataset that contains handwritten digits.
%

% Load Training Data
fprintf('Loading and Visualizing Data ...\n')

                            
my_data_val = readtable('my_data.txt') ;

X1 = my_data_val(: , 1:6 ) ;

y2 = my_data_val(:,7) ;

X = X1{:,:} ;
y2 = y2{:,:} ;

m = size(X, 1);

MP = containers.Map('KeyType','char', 'ValueType','int32') ;
MP2 = containers.Map('KeyType','int32', 'ValueType','char') ;

count1 = 1 ; 

for i = 1 : m 
    
    if( isKey( MP , y2(i,1) ) == 0 )
        
        MP( cast( y2(i,1) , 'char' ) ) = cast(count1,'int32') ;
        
        MP2( cast(count1,'int32') ) = cast( y2(i,1) , 'char' ) ; 
        
        count1 = count1 + 1 ;
        
    end
end



y = zeros( m , 1 )  ;

for i = 1 : m 
    
    y(i,1) =  MP( cast( y2(i,1) , 'char' ) )  ;
    
end

input_layer_size  = size(X,2);  
hidden_layer_size = 15*count1 ;   
num_labels = count1-1 ;   





%% ================ Part 6: Initializing Pameters ================
%  In this part of the exercise, you will be starting to implment a two
%  layer neural network that classifies digits. You will start by
%  implementing a function to initialize the weights of the neural network
%  (randInitializeWeights.m)

fprintf('\nInitializing Neural Network Parameters ...\n')

initial_Theta1 = randInitializeWeights(input_layer_size, hidden_layer_size);
initial_Theta2 = randInitializeWeights(hidden_layer_size, num_labels);

% Unroll parameters
initial_nn_params = [initial_Theta1(:) ; initial_Theta2(:)];



%% =============== Part 7: Implement Backpropagation ===============
%  Once your cost matches up with ours, you should proceed to implement the
%  backpropagation algorithm for the neural network. You should add to the
%  code you've written in nnCostFunction.m to return the partial
%  derivatives of the parameters.
%
fprintf('\nChecking Backpropagation... \n');

%  Check gradients by running checkNNGradients
checkNNGradients;



%% =============== Part 8: Implement Regularization ===============
%  Once your backpropagation implementation is correct, you should now
%  continue to implement the regularization with the cost and gradient.
%

fprintf('\nChecking Backpropagation (w/ Regularization) ... \n')

%  Check gradients by running checkNNGradients
lambda = 3;
checkNNGradients(lambda);



%% =================== Part 8: Training NN ===================
%  You have now implemented all the code necessary to train a neural 
%  network. To train your neural network, we will now use "fmincg", which
%  is a function which works similarly to "fminunc". Recall that these
%  advanced optimizers are able to train our cost functions efficiently as
%  long as we provide them with the gradient computations.
%
fprintf('\nTraining Neural Network... \n')

%  After you have completed the assignment, change the MaxIter to a larger
%  value to see how more training helps.
options = optimset('MaxIter', 9000);

%  You should also try different values of lambda
lambda = 0.85 ;

% Create "short hand" for the cost function to be minimized
costFunction = @(p) nnCostFunction(p, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, X, y, lambda);

% Now, costFunction is a function that takes in only one argument (the
% neural network parameters)
[nn_params, cost] = fmincg(costFunction, initial_nn_params, options);

% Obtain Theta1 and Theta2 back from nn_params
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));



%% ================= Part 9: Visualize Weights =================
%  You can now "visualize" what the neural network is learning by 
%  displaying the hidden units to see what features they are capturing in 
%  the data.

fprintf('\n Now giving input to Trained Model ... \n')

input_data = zeros(1,6) ;

input_data(1,1) = 20.87974371; %20.87974371,82.00274423,6.502985292000001,202.9355362 

input_data(1,2) = 82.00274423 ;
input_data(1,3) = 6.502985292000001;
input_data(1,4) = 202.9355362;
input_data(1,5) = 20;
input_data(1,6) = 13;

index_of_output = get_output( Theta1,Theta2,input_data ) ;

fprintf(' So the final output is   ') ;

index_of_output 

fprintf(' \n ' ) ;

MP2(cast(index_of_output,'int32'))


%% ================= Part 10: Implement Predict =================
%  After training the neural network, we would like to use it to predict
%  the labels. You will now implement the "predict" function to use the
%  neural network to predict the labels of the training set. This lets
%  you compute the training set accuracy.

pred = predict(Theta1, Theta2, X);

fprintf('\nTraining Set Accuracy: %f\n', mean(double( pred == y)) * 100);

fprintf(' %f %f\n ', size(Theta1) , size(Theta2) ) ;

dlmwrite('Theta1Val.xlsx',Theta1,'newline','pc') ;

dlmwrite('Theta2Val.xlsx',Theta2,'newline','pc') ;

fileID = fopen('mapval.txt','w');

for i = 1 : 100 
    
    if( isKey(MP2,i) == 0 ) 
        break ;
    end
    
    fprintf(fileID, ' %0.1f %s \n ',i,MP2(i)) ;
    
end

fclose(fileID);


fileID = fopen('diffcrops.txt','w');

for i = 1 : 100 
    
    if( isKey(MP2,i) == 0 ) 
        break ;
    end
    
    fprintf(fileID, ' %s \n ' ,MP2(i)) ;
    
end

fclose(fileID);
    
        



