import 'package:pigeon/pigeon.dart';

class SearchRequest {
  String query;
}

class SearchReply {
  String result;
}

@HostApi()
abstract class Api {
  SearchReply searchReply(SearchRequest request);
}
