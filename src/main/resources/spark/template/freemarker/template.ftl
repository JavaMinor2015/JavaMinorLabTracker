<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>1 Col Portfolio - Start Bootstrap Template</title>

    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
        .finish-assignment {
            width: 230px;
        }
        .table thead tr th {
            text-align: center;
        }

        .table tr th, .table tr td {
            border: 0px !important;
            vertical-align: middle !important;
        }
    </style>

</head>

<body>

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">LabTracker</a>
        </div>
    </div>
    <!-- /.container -->
</nav>

<br class="clear" />

<!-- Page Content -->
<div class="container">

    <!-- Page Heading -->
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">${header}</h1>
        </div>
    </div>
    <!-- /.row -->

    <!-- Project One
    <div class="row">
        <div class="col-md-7">
            <a href="#">
                <img class="img-responsive" src="http://placehold.it/700x300" alt="">
            </a>
        </div>
        <div class="col-md-5">
            <h3>Project One</h3>
            <h4>Subheading</h4>
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Laudantium veniam exercitationem expedita laborum at voluptate. Labore, voluptates totam at aut nemo deserunt rem magni pariatur quos perspiciatis atque eveniet unde.</p>
            <a class="btn btn-primary" href="#">View Project <span class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
    </div>
    <!-- /.row -->

    ${html}

    <hr>

    <!-- Footer -->
    <footer>
        <div class="row">
            <div class="col-lg-12">
                <p>Copyright &copy; Java Minor 2015</p>
            </div>
        </div>
        <!-- /.row -->
    </footer>

</div>
<!-- /.container -->

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script>

    var assignmentID = 0;

    $(document).ready(function(){
        $(".assignment-button").click(function(){
            var id = $(this).data("id");
            assignmentID = id;

            $("#assignment-data").html("Laden...");
            $.ajax({
                url: "/assignment.json",
                data: {id : id},
                type: "GET",
                dataType: "json"
            }).done(function(data){
                var html = "<table class=\"table table-striped\">";
                html += "<thead><tr><th>Opdracht: " + data.name + "</th><th>Telefoon</th><th>Email</th></tr></thead><tbody>";
                $.each(data.students, function(index, student){
                    html += "<tr><td><button class=\"btn btn-" + (student.status == 1 ? "success" : "danger") + " finish-assignment\" " +
                            "data-status=\"" + student.status + "\" " +
                            "data-student-id=\"" + student.id + "\">" + student.name + ": " + (student.status == 1 ? "Gemaakt" : "Niet gemaakt") + "!</button></td>";
                    html += "<td>" + (typeof(student.telephone) != "undefined" ? student.telephone : "NB.") + "</td>" +
                            "<td>" + (typeof(student.email) != "undefined" ? student.email : "NB.") + "</td>";
                    html += "</tr>";
                });
                html += "</tbody></table><a class=\"btn btn-primary\" href=\"/assign-student/?id=" + data.id + "\">Student toewijzen</a>";
                $("#assignment-data").html(html);
            });

        });

        $("#assignment-data").on("click", ".finish-assignment", function(){
            var student = $(this).data("student-id");
            var status = $(this).data("status");

            $.ajax({
                url: "/assignment-edit",
                data: { assignment: assignmentID, student: student, status: status },
                type: "POST"
            }).done(function(data){
                $(".assignment-button[data-id=" + assignmentID + "]").trigger("click");
            })
        });
    });

</script>

</body>

</html>
